import React from 'react';

export default function Navbar({ activeTab, setActiveTab }) {
    return (
        <>
            <header className="top-bar">
                <div className="logo-area">
                    <div className="logo-icon">⚕️</div>
                    <h1>PharmaLink Manager</h1>
                </div>
                <div>
                    <span className="badge badge-stock">Gateway: 8080</span>
                    <span id="warehouse-info" style={{ fontSize: '0.8rem', color: '#666', marginLeft: '10px' }}>
                        {/* Warehouse info populated dynamically in original, we can leave or pass as prop */}
                    </span>
                </div>
            </header>

            <div className="container" style={{ marginBottom: 0 }}>
                <div className="nav-tabs">
                    <button
                        className={`tab-btn ${activeTab === 'produits' ? 'active' : ''}`}
                        onClick={() => setActiveTab('produits')}
                    >
                        📦 Catalogue
                    </button>
                    <button
                        className={`tab-btn ${activeTab === 'commandes' ? 'active' : ''}`}
                        onClick={() => setActiveTab('commandes')}
                    >
                        🛒 Gestion Commandes
                    </button>
                    <button
                        className={`tab-btn ${activeTab === 'livraisons' ? 'active' : ''}`}
                        onClick={() => setActiveTab('livraisons')}
                    >
                        🚚 Expéditions
                    </button>
                </div>
            </div>
        </>
    );
}
