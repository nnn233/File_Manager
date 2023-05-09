# File_Manager

Данное приложение представляет собой файловый менеджер для доступа к файлам из внешенего хранилища устройства, сохраняя исходную файловую иерархию.

## Содержание
* [Установка](#download)
* [Процесс использования](#technologies)

## Процесс использования
### Доступ к файловому хранилищу
При первом запуске приложения пользователю предлагается разрешить доступ к внешнему хранилищу устройства. При его согласии на экране отображаются папки корневого каталога, в противном случае - сообщение о том, что файлы недоступны для просмотра. Также если пользователь отклоняет разрешение и ставит галочку "Больше не спрашивать" при повторном запуске приложения откроется диалоговое окно, предлагающее перейти в настройки и все-таки предоставить доступ к файлам.   
### Работа с приложением
В верхней части экрана находятся две иконки, при нажатии на которые отображаются меню для выбора способов сортировки и фильтрации данных.
Чуть ниже заголовка "Мои файлы" расположена кнопка для перехода к корневому каталогу и строка, отражающая путь до папки, в которой находится пользователь (если пользователь находится в корневом каталоге, путь не отображается).   
При нажатии на файла каталога происходит переход к файлам, содержащимся в этом каталоге. При нажатии на файл пользователю предлагается меню с возможностью выбора между открытием выбранного файла и его отправкой.   
