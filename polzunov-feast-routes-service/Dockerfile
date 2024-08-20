FROM python:3.8-slim

# Установить зависимости
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Копировать файлы приложения
COPY . /app
WORKDIR /app

# Установить переменные окружения
ENV FLASK_APP=run.py
ENV FLASK_ENV=development

# Открыть порт
EXPOSE 5000

# Запустить приложение
CMD ["flask", "run", "--host=0.0.0.0"]
