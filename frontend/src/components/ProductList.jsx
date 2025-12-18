import React, { useState, useEffect } from 'react';

export default function ProductList() {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchProducts = async () => {
        setLoading(true);
        setError(null);
        try {
            const res = await fetch('/api/produits');
            const data = await res.json();
            setProducts(data._embedded ? data._embedded.produits : []);
        } catch (e) {
            console.error(e);
            setError('Impossible de charger les produits');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchProducts();
    }, []);

    return (
        <div id="produits" className="tab-content active">
            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '20px' }}>
                <h2>Inventaire</h2>
                <button className="btn btn-secondary" onClick={fetchProducts}>🔄 Actualiser</button>
            </div>

            {loading && <p style={{ textAlign: 'center', color: '#666' }}>Chargement...</p>}
            {error && <p style={{ textAlign: 'center', color: 'red' }}>{error}</p>}

            <div className="products-grid" id="products-list">
                {products.map(p => (
                    <div className="product-card" key={p.id || p.nom}>
                        <h3>{p.nom}</h3>
                        <p style={{ color: '#666', fontSize: '0.9rem' }}>{p.description}</p>
                        <div className="product-price">{p.prix} MAD</div>
                        <span className={`badge ${p.quantiteStock > 0 ? 'badge-stock' : 'badge-low'}`}>
                            Stock: {p.quantiteStock}
                        </span>
                    </div>
                ))}
            </div>
        </div>
    );
}
