//
// Created by Dylan Meadows on 2020-03-14.
//

#include "SoundService.h"
#include <iostream>
#include <QtCore>

namespace service {
    QThread *mThread;

    static QSoundEffect *loadSound(const std::string &filename, QObject *parent = nullptr);

    SoundService::SoundService(const settings::ActionSettings *actionSettings,
                               QObject *parent)
        : QObject(parent),
          actionSettings(actionSettings) {
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

    QSoundEffect *loadSound(const std::string &filename, QObject *parent) {
        auto *sound = new QSoundEffect(parent);
        sound->setSource(QUrl::fromLocalFile(QString::fromStdString(filename)));
        return sound;
    }
}
