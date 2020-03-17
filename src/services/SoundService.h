//
// Created by Dylan Meadows on 2020-03-14.
//

#ifndef EONTIMER_SOUNDREGISTRY_H
#define EONTIMER_SOUNDREGISTRY_H

#include <QObject>
#include <services/settings/ActionSettings.h>
#include <SFML/Audio/Sound.hpp>

namespace service {
    class SoundService : public QObject {
    Q_OBJECT
    private:
        const settings::ActionSettings *actionSettings;
        sf::Sound *mBeep;
        sf::Sound *mDing;
        sf::Sound *mTick;
        sf::Sound *mPop;

    public:
        explicit SoundService(const settings::ActionSettings *actionSettings,
                              QObject *parent = nullptr);

        // @formatter:off
    public slots:
        void play();
        // @formatter:on
    };
}


#endif //EONTIMER_SOUNDREGISTRY_H
