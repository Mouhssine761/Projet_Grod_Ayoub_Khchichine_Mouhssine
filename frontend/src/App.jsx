import React, { useState } from 'react';
import Navbar from './components/Navbar';
import ProductList from './components/ProductList';
import OrderManager from './components/OrderManager';
import DeliveryDashboard from './components/DeliveryDashboard';

function App() {
  const [activeTab, setActiveTab] = useState('produits');

  return (
    <>
      <Navbar activeTab={activeTab} setActiveTab={setActiveTab} />

      <div className="container">
        {activeTab === 'produits' && <ProductList />}
        {activeTab === 'commandes' && <OrderManager />}
        {activeTab === 'livraisons' && <DeliveryDashboard />}
      </div>
    </>
  );
}

export default App;
