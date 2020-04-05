//
// Created by Dylan Meadows on 2020-03-14.
//

#include "SoundService.h"

#include <QResource>
#include <SFML/Audio/SoundBuffer.hpp>

namespace service {

    static sf::Sound *loadSound(const char *filename);

    SoundService::SoundService(
        const model::settings::ActionSettingsModel *actionSettings,
        QObject *parent)
        : QObject(parent), actionSettings(actionSettings) {
        mBeep = loadSound(":/sounds/beep.wav");
        mDing = loadSound(":/sounds/ding.wav");
        mTick = loadSound(":/sounds/tick.wav");
        mPop = loadSound(":/sounds/pop.wav");
    }

    void SoundService::play() {
        switch (actionSettings->getSound()) {
            case model::Sound::BEEP:
                mBeep->play();
                break;
            case model::Sound::DING:
                mDing->play();
                break;
            case model::Sound::TICK:
                mTick->play();
                break;
            case model::Sound::POP:
                mPop->play();
                break;
        }
    }

    sf::Sound *loadSound(const char *filename) {
        QResource resource(filename);
        auto *sound = new sf::Sound();
        auto *buffer = new sf::SoundBuffer();
        buffer->loadFromMemory(resource.data(),
                               static_cast<size_t>(resource.size()));
        sound->setBuffer(*buffer);
        return sound;
    }
}  // namespace service
