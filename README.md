# Fastest

#### Виконують :
Бериславський Владислав АІ-204, Сіренко Марія АІ-204
### Завдання
Система створення та преревірки тестів. В систему завантажують документ з питаннями та варіантами відповідей (правильний виділяють за правилами системи).
Система генерує список pdf-файлів с білетами та pdf-файли з бланками відповідей, де студент відмічае свої відповіді. 
Використовуючи додадок викладач фотографуе бланкі відповідей студентів та отримує оцінку.

<br><br>
Дизайн додатку - [посидання на дизайн у Figma](https://www.figma.com/file/qmm2YXCDbmeLv3oH59SU7Q/Untitled?node-id=0%3A1) <br>
Беклог продукту - [посилання та Google Table](https://docs.google.com/spreadsheets/d/1UuI6Cx85vHR5UQqz2wBgwwStmLcmdR46wQpE9lVLe_0/edit?usp=sharing) <br>
Беклог першого спринта - [посилання та Google Table](https://docs.google.com/spreadsheets/d/1LliFfLlcia2bYayfSoojQYUXv9Tpz1fcZ5TrDVyhLJg/edit?usp=sharing)<br>
Скрипт створення Бази Даних - [посилання та Google Диск](https://drive.google.com/file/d/1uK-W2XiGN-MXVPmWYgcTwCwHwt-3rQJI/view?usp=sharing)<br>

<br><br>
## Бачення системи
ER-діаграмма бази данних
![Диаграма](https://github.com/mariiasirenko2/Fastest/blob/master/img/ER_diagramm_Fastest.png)


Згідно до інформації про дані, що зберігаються в одній з баз даних на мільйони користувачів:
<ul>
  <li>Довжина імен, що зберігаються у базі не перевищує 25 символів;</li>
  <li>Прізвища займають довжину, аналогічну до імен;</li>
  <li>Найдовша електронна адреса містить 62 символи.</li>
 </ul>
 Саме такі значення оберемо для обмеження довжин символьних типів даних.
