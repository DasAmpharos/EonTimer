//
// Created by Dylan Meadows on 2020-03-14.
//

#ifndef EONTIMER_SOUNDREGISTRY_H
#define EONTIMER_SOUNDREGISTRY_H

#include <QObject>
#include <QThread>
#include <services/settings/ActionSettings.h>
#include <QSoundEffect>
#include <util/WorkerThread.h>

namespace service {
    class SoundService : public QObject {
    Q_OBJECT
    private:
        util::WorkerThread *workerThread;
        const settings::ActionSettings *actionSettings;
        QSoundEffect *mBeep;
        QSoundEffect *mDing;
        QSoundEffect *mTick;
        QSoundEffect *mPop;

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
