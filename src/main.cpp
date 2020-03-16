#include <qapplication.h>
#include <gui/ApplicationWindow.h>
#include <QThreadPool>

int main(int argc, char **argv) {
    QApplication app(argc, argv);
    QCoreApplication::setApplicationName("EonTimer");
    QCoreApplication::setOrganizationName("DylanMeadows");
    QCoreApplication::setOrganizationDomain("io.github.dylmeadows");
    QThreadPool::globalInstance()->setExpiryTimeout(-1);

    gui::ApplicationWindow window;
    window.show();

    return QApplication::exec();
}