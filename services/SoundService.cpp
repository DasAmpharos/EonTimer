//
// Created by Dylan Meadows on 2020-03-14.
//

#include "SoundService.h"
#include "SettingsService.h"
#include <iostream>

namespace service {
    static QSoundEffect *loadSound(const std::string &filename);

    SoundService::SoundService(QObject *parent)
        : QObject(parent),
          mBeep(loadSound("beep.wav")) {
    }

    void SoundService::play(const model::Sound sound) const {
        switch (sound) {
            case model::Sound::BEEP:
                mBeep->play();
                break;
        }
    }

    void SoundService::play() const {
        const auto settings = SettingsService::get();
        play(settings.getSound());
    }

    QSoundEffect *loadSound(const std::string &filename) {
        auto *sound = new QSoundEffect();
        sound->setSource(QUrl::fromLocalFile(QString::fromStdString(filename)));
        return sound;
    }
}
