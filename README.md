# Установка приложения
Чтобы установить приложение загрузите [apk-файл]([https://github.com/Vpyc/TestMusicPlayer/releases/download/1.0.0/TestMusicPlayer.apk]). После загрузки, установите его на своё мобильное устройство.
Приложение лучше запускать при включённой тёмной теме телефона, т.к. приложение разрабатывалось в основном под тёмную тему телефона.

# Во время разработки столкнулся со следующими проблемами
1. Было не понятно как передавать данные о песне в экран воспроизведения. Решил по средством загрузки всех данных через Intent, предварительно сделав их Parselable.
2. Были сложности с подключением Сервиса к ViewModel, чтобы можно было отрисовывать трэк на экране воспроизведения. Для решения данной проблемы добавил Репозиторий, через который взаимодействую с сервисом
