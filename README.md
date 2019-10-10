Система Библиотека
==================
#### Создайте Каталог, по которому можно искать по:
- Автору(одному из группы).
- Названию книги или её фрагменте.
- Одному из ключевых слов книги (атрибут книги).

Каталог книг заполняет Администратор, добавляя и изменяя/удаляя их. Каждая книга должна 
иметь адрес (место на полке) или читателя. Читатель чтобы взять книгу регистрируется, 
оставляя э-мейл и номер телефона. Книга может быть взята у Администратора в библиотеке 
на время не более месяца, только в случае если книга доступна в библиотекею Администратор
должен иметь страницу где отражаются взятые книги и читатели, которые пользуются книгой.

#### Для запуска проекта:
* git clone https://github.com/tolik1106/springboot-library.git
* create mysql schema 'sample'
* run schema.sql and data.sql
* run mvn package
* run executable jar fire