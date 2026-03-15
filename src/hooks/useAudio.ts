import {useCallback, useEffect, useRef, useState} from 'react';

const THRESHOLD = 60;
const SILENCE_CENTER = 128;

interface UseAudioOptions {
    onDetect?: () => void;
    threshold?: number;
}

export const useAudio = (options?: UseAudioOptions) => {
    const [isDetected, setIsDetected] = useState(false);
    const [isListening, setIsListening] = useState(false);

    const audioContextRef = useRef<AudioContext | null>(null);
    const analyserRef = useRef<AnalyserNode | null>(null);
    const sourceRef = useRef<MediaStreamAudioSourceNode | null>(null);
    const streamRef = useRef<MediaStream | null>(null);
    const requestRef = useRef<number | null>(null);
    const detectedRef = useRef(false);
    const onDetectRef = useRef<UseAudioOptions['onDetect']>(options?.onDetect);

    useEffect(() => {
        onDetectRef.current = options?.onDetect;
    }, [options?.onDetect]);

    function calculateMaxDeviation(dataArray: Uint8Array) {
        let maxDeviation = 0;

        for (let i = 0; i < dataArray.length; i++) {
            const deviation = Math.abs(dataArray[i] - SILENCE_CENTER);
            if (deviation > maxDeviation) {
                maxDeviation = deviation;
            }
        }

        return maxDeviation;
    }

    const analyzeSound = useCallback(() => {
        const analyser = analyserRef.current;
        if (!analyser) {
            return;
        }

        const dataArray = new Uint8Array(analyser.frequencyBinCount);
        analyser.getByteTimeDomainData(dataArray);

        const maxDeviation = calculateMaxDeviation(dataArray);
        const threshold = options?.threshold ?? THRESHOLD;
        if (maxDeviation > threshold && !detectedRef.current) {
            detectedRef.current = true;
            setIsDetected(true);
            onDetectRef.current?.();
        }

        requestRef.current = requestAnimationFrame(analyzeSound);
    }, [options?.threshold]);

    const stopListening = useCallback(() => {
        if (requestRef.current !== null) {
            cancelAnimationFrame(requestRef.current);
            requestRef.current = null;
        }

        sourceRef.current?.disconnect();
        sourceRef.current = null;

        analyserRef.current?.disconnect();
        analyserRef.current = null;

        streamRef.current?.getTracks().forEach((track) => track.stop());
        streamRef.current = null;

        if (audioContextRef.current) {
            void audioContextRef.current.close();
            audioContextRef.current = null;
        }

        detectedRef.current = false;
        setIsListening(false);
        setIsDetected(false);
    }, []);

    const startListening = async () => {
        if (isListening) {
            return;
        }

        try {
            const stream = await navigator.mediaDevices.getUserMedia({ audio: true });

            const audioContext = new window.AudioContext();
            const analyser = audioContext.createAnalyser();
            const source = audioContext.createMediaStreamSource(stream);

            source.connect(analyser);

            audioContextRef.current = audioContext;
            analyserRef.current = analyser;
            sourceRef.current = source;
            streamRef.current = stream;

            detectedRef.current = false;
            setIsListening(true);
            setIsDetected(false);
            analyzeSound();
        } catch (error) {
            alert('Error accessing microphone: ' + error);
            stopListening();
        }
    };

    useEffect(() => stopListening, [stopListening]);

    return {isListening, isDetected, startListening, stopListening};
};
