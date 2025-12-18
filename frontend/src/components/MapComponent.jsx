import React, { useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

// Fix Leaflet icon issues
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
    iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
    iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
});

// Custom Icons
const warehouseIcon = new L.Icon({
    iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-blue.png',
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41]
});

const deliveryIcon = new L.Icon({
    iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-red.png',
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41]
});

function MapController({ center }) {
    const map = useMap();
    useEffect(() => {
        if (center) {
            map.setView(center, 10);
            map.invalidateSize();
        }
    }, [center, map]);
    return null;
}

export default function MapComponent({ warehouseLocation, deliveries }) {
    // Memoize center to prevent infinite loops in effects
    const center = React.useMemo(() => {
        if (warehouseLocation && typeof warehouseLocation.lat === 'number' && typeof warehouseLocation.lon === 'number') {
            return [warehouseLocation.lat, warehouseLocation.lon];
        }
        return [46.603354, 1.888334]; // Default (France)
    }, [warehouseLocation]); // assuming warehouseLocation object reference changes only when data changes

    // ensure valid lists
    const validDeliveries = React.useMemo(() => {
        if (!Array.isArray(deliveries)) return [];
        return deliveries.filter(d => d && typeof d.latitude === 'number' && typeof d.longitude === 'number');
    }, [deliveries]);

    return (
        <div className="map-wrapper">
            <MapContainer center={center} zoom={6} style={{ height: '100%', width: '100%' }}>
                <TileLayer
                    attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />

                {/* Markers */}
                {warehouseLocation && typeof warehouseLocation.lat === 'number' && (
                    <Marker position={[warehouseLocation.lat, warehouseLocation.lon]} icon={warehouseIcon}>
                        <Popup><b>Entrepôt Principal</b></Popup>
                    </Marker>
                )}

                {validDeliveries.map(d => (
                    <Marker key={d.id} position={[d.latitude, d.longitude]} icon={deliveryIcon}>
                        <Popup>
                            <b>Livraison #{d.id}</b><br />
                            {d.adresseLivraison}
                        </Popup>
                    </Marker>
                ))}

                <MapController center={center} />
            </MapContainer>
        </div>
    );
}
