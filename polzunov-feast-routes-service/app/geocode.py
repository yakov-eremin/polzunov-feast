from geopy.geocoders import Nominatim
from geopy.exc import GeocoderTimedOut, GeocoderServiceError
from app.graph import driver

geolocator = Nominatim(user_agent="orienternet")

def fetch_address_details(address):
    try:
        location = geolocator.geocode(address)
        if location:
            return (location.latitude, location.longitude, location.address)
        else:
            return None
    except (GeocoderTimedOut, GeocoderServiceError) as e:
        print(f"Ошибка при геокодировании {address}: {e}")
        return None

def save_address_to_neo4j(address, latitude, longitude, full_address):
    with driver.session() as session:
        session.run(
            '''
            MERGE (a:Address {full_address: $full_address})
            SET a.latitude = $latitude, a.longitude = $longitude, a.location = point({latitude: $latitude, longitude: $longitude})
            ''',
            full_address=full_address, latitude=latitude, longitude=longitude
        )