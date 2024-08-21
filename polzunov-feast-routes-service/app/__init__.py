from flask import Flask
from app.config import Config
from .graph import load_graphs
def create_app():
    app = Flask(__name__, template_folder='../templates')
    app.config.from_object(Config)
    with app.app_context():
        from . import routes
        load_graphs()
    return app