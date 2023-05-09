# FileManager

Данное приложение представляет собой файловый менеджер для доступа к файлам из внешенего хранилища устройства, сохраняя исходную файловую иерархию. При этом файлы можно просматривать, делиться ими, но нельзя перемещать или удалять. Приложение подходит для устройств с операционной системой Андроид.

## Содержание
* [Установка](#download)
* [Процесс использования](#technologies)

## Установка
Для того чтобы установить приложение необходимо скачать apk-файл и загрузить на мобильное устройство с ОС Андроид. Предварительно, необходимо в настройках телефона разрешить установку приложений из внешних источников.   

## Процесс использования
### Доступ к файловому хранилищу
При первом запуске приложения пользователю предлагается разрешить доступ к внешнему хранилищу устройства. При его согласии на экране отображаются папки корневого каталога, в противном случае - сообщение о том, что файлы недоступны для просмотра.   
![запрос разрешения](https://github.com/nnn233/File_Manager/assets/126052177/591d2ee5-1089-438b-b56a-61bc815c606e)   
Также если пользователь отклоняет разрешение и ставит галочку "Больше не спрашивать" при повторном запуске приложения откроется диалоговое окно, предлагающее перейти в настройки и все-таки предоставить доступ к файлам.   
![сбой](https://github.com/nnn233/File_Manager/assets/126052177/e032691c-3bcf-4c72-9876-675e632a0eb0)   

### Работа с приложением
Все файлы в каталоге по умолчанию отсортированы в алфавитном порядке.    
Чуть ниже заголовка "Мои файлы" расположена кнопка для перехода к корневому каталогу и строка, отражающая путь до папки, в которой находится пользователь (если пользователь находится в корневом каталоге, путь не отображается). Возврат на шаг назад осуществляется с помощью системной кнопки Back.  
![экран](https://github.com/nnn233/File_Manager/assets/126052177/821dd22b-6175-420b-aef7-f2b47ecd3bad)   

В верхней части экрана находятся две иконки, при нажатии на которые отображаются меню для выбора способов сортировки и фильтрации данных.   
![сортировка меню](https://github.com/nnn233/File_Manager/assets/126052177/420f6238-e7a5-447b-8baf-6bac75ab3ce1)
![фильтр меню](https://github.com/nnn233/File_Manager/assets/126052177/cbb8353e-f996-4956-904a-7d0a02ddfe02)   

При нажатии на файла каталога происходит переход к файлам, содержащимся в этом каталоге. При нажатии на файл пользователю предлагается меню с возможностью выбора между открытием выбранного файла и его отправкой.   
![меню для файла](https://github.com/nnn233/File_Manager/assets/126052177/75c38b3d-1db8-40b6-8a12-6c261603d82b)

