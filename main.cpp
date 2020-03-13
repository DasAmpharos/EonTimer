#include <qapplication.h>
#include <gui/ApplicationWindow.h>

int main(int argc, char **argv) {
    QApplication app(argc, argv);

    gui::ApplicationWindow window;
    window.show();

    return QApplication::exec();
}