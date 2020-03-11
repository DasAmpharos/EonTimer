#include <qapplication.h>
#include "ui/ApplicationWindow.h"

int main(int argc, char **argv) {
    QApplication app(argc, argv);

    ui::ApplicationWindow window;
    window.show();

    return app.exec();
}