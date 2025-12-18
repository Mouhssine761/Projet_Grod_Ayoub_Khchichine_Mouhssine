import React, { useState, useEffect } from 'react';
import MapComponent from './MapComponent';
import ErrorBoundary from './ErrorBoundary';

export default function DeliveryDashboard() {
    const [deliveries, setDeliveries] = useState([]);
    const [warehouseLocation, setWarehouseLocation] = useState(null);
    const [formData, setFormData] = useState({
        adresseLivraison: '',
        ville: '',
        codePostal: '',
        pays: 'France',
        transporteur: 'DHL'
    });
    const [notification, setNotification] = useState('');

    const fetchConfig = async () => {
        try {
            const res = await fetch('/api/livraisons/config');
            const data = await res.json();
            setWarehouseLocation({ lat: data.latitude, lon: data.longitude });
        } catch (e) {
            console.error("Failed to load config", e);
        }
    };

    const fetchDeliveries = async () => {
        try {
            const res = await fetch('/api/livraisons');
            const data = await res.json();
            setDeliveries(Array.isArray(data) ? data.reverse() : []);
        } catch (e) {
            console.error("Failed to load deliveries", e);
        }
    };

    useEffect(() => {
        fetchConfig();
        fetchDeliveries();
    }, []);

    const handleInputChange = (e) => {
        const { id, value } = e.target;
        setFormData(prev => ({ ...prev, [id]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const res = await fetch('/api/livraisons', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(formData)
            });

            if (res.ok) {
                showNotif("✅ Livraison créée !");
                setFormData({
                    adresseLivraison: '',
                    ville: '',
                    codePostal: '',
                    pays: 'France',
                    transporteur: 'DHL'
                });
                fetchDeliveries();
            } else {
                showNotif("❌ Erreur lors de la création");
            }
        } catch (err) {
            console.error(err);
            showNotif("❌ Erreur réseau");
        }
    };

    const showNotif = (msg) => {
        setNotification(msg);
        setTimeout(() => setNotification(''), 3000);
    };

    return (
        <div id="livraisons" className="tab-content active">
            <div className="livraison-wrapper">
                <div className="l-sidebar">
                    <div className="l-sidebar-header">
                        <h2>Nouvelle Livraison</h2>
                        <form onSubmit={handleSubmit} className="new-delivery-form">
                            <div className="form-group" style={{ marginBottom: '0.5rem' }}>

                                <input
                                    type="text"
                                    id="adresseLivraison"
                                    placeholder="Adresse (ex: 123 Rue Principale)"
                                    required
                                    value={formData.adresseLivraison}
                                    onChange={handleInputChange}
                                />
                            </div>
                            <div className="form-group" style={{ marginBottom: '0.5rem' }}>
                                <input
                                    type="text"
                                    id="ville"
                                    placeholder="Ville (ex: Paris)"
                                    required
                                    value={formData.ville}
                                    onChange={handleInputChange}
                                />
                            </div>
                            <div className="form-group" style={{ marginBottom: '0.5rem' }}>
                                <input
                                    type="text"
                                    id="codePostal"
                                    placeholder="Code Postal (ex: 75001)"
                                    required
                                    value={formData.codePostal}
                                    onChange={handleInputChange}
                                />
                            </div>
                            <div className="form-group" style={{ marginBottom: '0.5rem' }}>
                                <input
                                    type="text"
                                    id="pays"
                                    placeholder="Pays (ex: France)"
                                    required
                                    value={formData.pays}
                                    onChange={handleInputChange}
                                />
                            </div>
                            <div className="form-group" style={{ marginBottom: '0.5rem' }}>
                                <input
                                    type="text"
                                    id="transporteur"
                                    placeholder="Transporteur"
                                    value={formData.transporteur}
                                    onChange={handleInputChange}
                                />
                            </div>
                            <button type="submit" className="btn btn-primary">Créer Livraison</button>
                        </form>
                    </div>

                    <div id="delivery-list" className="delivery-list">
                        {deliveries.length === 0 && <p style={{ padding: '20px', textAlign: 'center', color: '#999' }}>Aucune livraison</p>}
                        {deliveries.map(d => (
                            <div key={d.id} className="delivery-item">
                                <div className="delivery-header">
                                    <span className="delivery-id">#{d.id}</span>
                                    <span className={`delivery-status status-${d.statut}`}>{d.statut}</span>
                                </div>
                                <div className="delivery-address">{d.adresseLivraison}, {d.ville}</div>
                                <div className="delivery-meta">
                                    <span>{d.transporteur}</span>
                                    <span>{d.dateExpedition ? new Date(d.dateExpedition).toLocaleDateString() : '-'}</span>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>

                <ErrorBoundary>
                    <MapComponent warehouseLocation={warehouseLocation} deliveries={deliveries} />
                </ErrorBoundary>
            </div>

            <div className={`notification ${notification ? 'show' : ''}`}>
                {notification}
            </div>
        </div>
    );
}
