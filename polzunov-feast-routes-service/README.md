# routes_webclient
Для развёртки проекта понадобятся следующие инструменты: Python 3.8+, Neo4j 4.0+, Docker и Docker Compose, Git.

1) Клонируйте репозиторий проекта:

git clone https://github.com/Gan-Salo/routes_webclientv
cd routes_webclient

2) Создайте виртуальное окружение и активируйте его:

python -m venv venv
venv\Scripts\activate

3) Установите зависимости, используя файл requirements.txt:

pip install -r requirements.txt

4) Используйте Docker Compose для запуска Neo4j:

docker-compose up

5) Загрузите данные в базу данных Neo4j, используя скрипт load_data.py:

python data/load_data.py

6) Запустите Flask приложение:

python run.py

7) Приложение будет доступно по адресу http://localhost:8081.