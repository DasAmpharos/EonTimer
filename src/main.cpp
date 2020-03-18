#include <qapplication.h>
#include <gui/ApplicationWindow.h>
#include <app.h>

int main(int argc, char **argv) {
    QApplication app(argc, argv);
    QCoreApplication::setApplicationName(APP_NAME);
    QCoreApplication::setOrganizationName("DylanMeadows");
    QCoreApplication::setOrganizationDomain("io.github.dylmeadows");

    gui::ApplicationWindow window;
    window.show();

    return QApplication::exec();
}