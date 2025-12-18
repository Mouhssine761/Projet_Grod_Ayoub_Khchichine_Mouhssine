import React, { useState, useEffect } from 'react';

export default function OrderManager() {
    const [orders, setOrders] = useState([]);
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(false);

    // New Order Form State
    const [clientNom, setClientNom] = useState('');
    const [orderLines, setOrderLines] = useState([]); // Array of { tempId, productId, quantity }
    const [notification, setNotification] = useState('');

    const fetchOrders = async () => {
        setLoading(true);
        try {
            const res = await fetch('/api/commandes');
            if (!res.ok) throw new Error('Failed to fetch');
            const data = await res.json();

            let fetchedOrders = [];
            if (data._embedded && data._embedded.commandes) {
                fetchedOrders = data._embedded.commandes;
            } else if (Array.isArray(data)) {
                fetchedOrders = data;
            }
            setOrders(fetchedOrders.reverse());
        } catch (e) {
            console.error(e);
        } finally {
            setLoading(false);
        }
    };

    const fetchProducts = async () => {
        try {
            const res = await fetch('/api/produits');
            const data = await res.json();
            setProducts(data._embedded ? data._embedded.produits : []);
        } catch (e) {
            console.error(e);
        }
    };

    useEffect(() => {
        fetchOrders();
        fetchProducts();
    }, []);

    const addOrderLine = () => {
        setOrderLines([...orderLines, { tempId: Date.now(), productId: '', quantity: 1 }]);
    };

    const removeOrderLine = (tempId) => {
        setOrderLines(orderLines.filter(line => line.tempId !== tempId));
    };

    const updateLine = (tempId, field, value) => {
        setOrderLines(orderLines.map(line =>
            line.tempId === tempId ? { ...line, [field]: value } : line
        ));
    };

    const calculateTotal = () => {
        return orderLines.reduce((sum, line) => {
            const product = products.find(p => String(p.id) === String(line.productId));
            return sum + (product ? product.prix * line.quantity : 0);
        }, 0);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const cleanLines = orderLines
            .filter(l => l.productId)
            .map(l => ({ produitId: l.productId, quantite: parseInt(l.quantity) }));

        if (cleanLines.length === 0) {
            showNotif('Veuillez ajouter des produits');
            return;
        }

        try {
            const res = await fetch('/api/commandes', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    clientNom,
                    clientEmail: "client@test.com",
                    lignes: cleanLines
                })
            });

            if (res.ok) {
                showNotif("✅ Commande créée !");
                setClientNom('');
                setOrderLines([]);
                fetchOrders();
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
        <div id="commandes" className="tab-content active">
            <div className="form-section">
                <h2 style={{ marginBottom: '20px' }}>Nouvelle Commande</h2>
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Client</label>
                        <input
                            type="text"
                            required
                            placeholder="Nom du client"
                            value={clientNom}
                            onChange={e => setClientNom(e.target.value)}
                        />
                    </div>

                    <div style={{ background: '#f8fafc', padding: '15px', borderRadius: '8px', marginBottom: '15px' }}>
                        <div id="order-lines">
                            {orderLines.map(line => (
                                <div key={line.tempId} style={{ display: 'flex', gap: '10px', marginBottom: '10px' }}>
                                    <select
                                        style={{ flex: 1 }}
                                        value={line.productId}
                                        onChange={e => updateLine(line.tempId, 'productId', e.target.value)}
                                    >
                                        <option value="">Choisir produit...</option>
                                        {products.map(p => (
                                            <option key={p.id} value={p.id}>
                                                {p.nom} ({p.quantiteStock}) - {p.prix} MAD
                                            </option>
                                        ))}
                                    </select>
                                    <input
                                        type="number"
                                        min="1"
                                        value={line.quantity}
                                        onChange={e => updateLine(line.tempId, 'quantity', e.target.value)}
                                        style={{ width: '80px' }}
                                    />
                                    <button type="button" className="btn btn-danger" onClick={() => removeOrderLine(line.tempId)}>×</button>
                                </div>
                            ))}
                        </div>

                        <button type="button" className="btn btn-secondary" onClick={addOrderLine} style={{ fontSize: '0.9rem', marginTop: '10px' }}>
                            + Ajouter Produit
                        </button>
                    </div>

                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                        <span style={{ fontSize: '1.2rem', fontWeight: 'bold' }}>
                            Total: {calculateTotal().toFixed(2)} MAD
                        </span>
                        <button type="submit" className="btn btn-primary">Valider la Commande</button>
                    </div>
                </form>
            </div>

            <div className="orders-container">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '15px' }}>
                    <h3>Historique des Commandes</h3>
                    <button className="btn btn-secondary" onClick={fetchOrders}>🔄 Actualiser</button>
                </div>

                <div id="orders-list">
                    {loading ? (
                        <p style={{ textAlign: 'center', color: '#666' }}>⏳ Chargement...</p>
                    ) : orders.length === 0 ? (
                        <p style={{ textAlign: 'center', color: '#999' }}>Aucune commande trouvée.</p>
                    ) : (
                        orders.map(c => (
                            <div key={c.id} className={`order-card ${c.statut}`}>
                                <div className="order-info">
                                    <h4>Commande #{c.id || 'Ref'} - {c.clientNom || 'Client'}</h4>
                                    <div className="order-meta">
                                        📅 {c.dateCommande ? new Date(c.dateCommande).toLocaleDateString() : 'Date inconnue'}
                                        &nbsp;| Statut: <strong>{c.statut}</strong>
                                    </div>
                                </div>
                                <div className="order-total">{c.montantTotal ? c.montantTotal.toFixed(2) : '0.00'} MAD</div>
                            </div>
                        ))
                    )}
                </div>
            </div>

            <div className={`notification ${notification ? 'show' : ''}`}>
                {notification}
            </div>
        </div>
    );
}
