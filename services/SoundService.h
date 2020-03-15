//
// Created by Dylan Meadows on 2020-03-14.
//

#ifndef EONTIMER_SOUNDREGISTRY_H
#define EONTIMER_SOUNDREGISTRY_H

#include <string>
#include <models/Sound.h>
#include <QSoundEffect>

namespace service {
    class SoundService : public QObject {
    Q_OBJECT
    private:
        QSoundEffect *mBeep;

    public:
        explicit SoundService(QObject *parent = nullptr);

        // @formatter:off
    public slots:
        void play(model::Sound sound) const;
        void play() const;
        // @formatter:on
    };
}


#endif //EONTIMER_SOUNDREGISTRY_H
