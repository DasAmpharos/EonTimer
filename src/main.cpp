#include <qapplication.h>
#include <gui/ApplicationWindow.h>

int main(int argc, char **argv) {
    QApplication app(argc, argv);
    QCoreApplication::setApplicationName("EonTimer");
    QCoreApplication::setOrganizationName("DylanMeadows");
    QCoreApplication::setOrganizationDomain("io.github.dylmeadows");

    gui::ApplicationWindow window;
    window.show();

    return QApplication::exec();
}