import React, { useState } from "react";
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import UserLogin from "./component/UserLogin";
import UserRegistration from "./component/UserRegistration";
import Messenger from "./component/Messenger";

function App() {

  return (
    <Router>
    
            <Routes>
                <Route path="/" element={<UserLogin />} />
                <Route path="/login" element={<UserLogin />} />
                <Route path="/register" element={<UserRegistration />} />
                <Route path="/messenger" element={<Messenger />} />
            </Routes>
        
    </Router>
);
}

export default App;
