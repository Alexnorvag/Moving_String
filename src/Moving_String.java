import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ItemEvent;

/**
 * Created by norvag on 13.04.2017.
 */
public class Moving_String extends Applet implements Runnable {
    Thread thread = null;                   //поток

    String shiftLine;                       //движущаяся строка
    String fontName;                        //имя шрифта
    String [] fonts;                        //массив всех шрифтов

    int StrLength;                          //длина строки
    int TitleHeight;                        //высота строки
    int PosX;                               //позиция по X
    int PosY;                               //позиция по Y
    int VelX;                               //скорость по X
    int VelY;                               //скорость по Y

    JPanel panel;                           //панель для добавления компонентов(списка шрифтов)
    JComboBox fontsList;                    //компонент списка шрифтов

    public void init() {                    //метод для инициализации всего
        Graphics g = getGraphics();         //создание графической переменной

        shiftLine = "My String.";           //строка, которая будет двигаться
        g.setFont(new Font("Arial", Font.PLAIN, 50));          //выбор начального шрифта
        FontMetrics fm = getFontMetrics(g.getFont());                   //записываем в fm описание шрифта

        StrLength = fm.stringWidth(shiftLine);                          //получен размер строки
        TitleHeight = fm.getHeight();                                   //высота строки

        PosX = getSize().width - StrLength;                             //начальная позиция(ширина апплета - размер строки)
        PosY = getSize().height - TitleHeight + 50;                     //начальная позиция(высота апплета - высота строки + размер шрифта)
        VelX = 3;                                                       //скорость икса
        VelY = 3;                                                       //скорость игрика

        panel = new JPanel();                                           //создаём панель

        fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();    //получен список всех шрифтов

        fontsList = new JComboBox(fonts);                               //добавили в выпадающий список все шрифты

        panel.add(fontsList);                                           //добавили в панель выпадающий список
        add(panel);                                                     //размещение в апплете

        fontsList.addItemListener(e -> {                                //слушатель для списка
            if (e.getStateChange() == ItemEvent.SELECTED) {             //если тип состояния совпадает с типом "выбран"
                fontName = fonts[fontsList.getSelectedIndex()];         //то имя шрифта возьмем из нашего списка шрифтов под номером, который мы выбрали в выпадающем списке апплета
            }
        });

    }

    public void paint(Graphics g){                                      //метод по отрисовке
        super.paint(g);                                                 //обращение к главному методу для нормального отображения компонентов
        g.setFont(new Font(fontName, Font.PLAIN, 50));              //подаём нужный шрифт и рисуем его

        g.drawString(shiftLine, PosX, PosY);                            //отрисовка строки в заданных координатах
        movement();                                                     //метод описывающий движение строки
    }

    public void movement() {                                            //метод описывающий движение строки
        PosX += VelX;                                                   //к текущей позиции по иксу добавим его скорость
        PosY += VelY;                                                   //к текущей позиции по игрику добавим его скорость

        if (PosX > getSize().width - StrLength) {                       //если позиция по иксу больше ширины апплета - длина строки
            PosX = getSize().width - StrLength;                         //то установим позицию по иксу
            VelX = -VelX;                                               //а скорость поменяем на противоположную
            chooseReg();                                                //выбор регистра для букв в строке
        } else if (PosX < 0) {                                          //если позиция икса меньше нуля
            PosX = 0;                                                   //установим позицию в иксе в ноль
            VelX = -VelX;                                               //скорость изменим на противоположную
            chooseReg();                                                //выбор регистра для букв в строке
        }

        if (PosY >= getSize().height - TitleHeight + 50) {              ///*то же самое что и раньше только для игрика
            PosY = getSize().height - TitleHeight + 50;                 //то же самое что и раньше только для игрика
            VelY = -VelY;                                               //то же самое что и раньше только для игрика
            chooseReg();                                                //то же самое что и раньше только для игрика
        } else if (PosY < 0) {                                          //то же самое что и раньше только для игрика
            PosY = 0;                                                   //то же самое что и раньше только для игрика
            VelY = -VelY;                                               //то же самое что и раньше только для игрика
            chooseReg();                                                //*/то же самое что и раньше только для игрика
        }
    }

    public void chooseReg() {                                           //метод позволяющий выбрать регистр для буквы случайным образом
        char [] chars = shiftLine.toCharArray();                        //перевод из типа "string" в тип "char" и запись в переменную chars
        shiftLine = "";                                                 //инициализируем пустой строкой
        for(char c: chars)                                              //цикл по перебору всех букв в массиве
        {
            if (select() == 0)                                          //если метод ыернул 0
                c = Character.toUpperCase(c);                           //то буква изменит свой регистр на верхний
            else                                                        //в противном случае
                c = Character.toLowerCase(c);                           //буква изменит свой регистр на нижний
            shiftLine += c;                                             //восстанавливаем строку добавляя в конец получившиеся символы
        }
    }

    public int select() {                                               //метод случайного выбора
        int var = (int)(Math.random()* 2);                              //запись в переменную псевдослучайного числаот 0 до 1
        return var;                                                     //вернём полученное число
    }

    public void start()                                                 //поточный метод, который запускает поток
    {
        if (thread == null)                                             //если поток пуст
        {
            thread = new Thread(this);                            //инициализируем его данным потоком
            thread.start();                                             //запуск потока
        }
    }

    public void stop()                                                  //метод остановки потока
    {
        if (thread != null)                                             //если поток не пуст
        {
            thread.stop();                                              //остановим его
            thread = null;                                              //и очистим
        }
    }

    public void run() {                                                 //метод из интерфейса раннабл
        while (true) {                                                  //пока истина
            try {                                                       //пытаемся
                repaint();                                              //перерисовать апплет
                Thread.sleep(30);                                  //усыпляем поток на 30 миллисекунд
            } catch (InterruptedException e) {                          //ловим исключение
                stop();                                                 //стопим поток
            }
        }
    }
}
