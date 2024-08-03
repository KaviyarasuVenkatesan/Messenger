import React,{useState} from 'react'
import axios from 'axios';
import '../userLogin.css'
import { useNavigate,Link } from 'react-router-dom';

const API_BASE_URL = 'http://localhost:8080';

const UserLogin = () => {

    const navigate = useNavigate()

    const [formData, setFormData] = useState({
        email: '',
        password: ''
    });

    const [errorMessage, setErrorMessage] = useState("")

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {

            const response = await axios.post(`${API_BASE_URL}/login`, formData)
            const token = response.data.body

            if(response.status === 200){
                console.log('Login successful:', response);
                sessionStorage.setItem('token', token)
                navigate('/messenger')
            }
            
        } catch (error) {

            if(error.response && error.response.status === 401)
                setErrorMessage('Email and Password not matching')
            else
                setErrorMessage('error occured, please try again later')

            console.error('Login failed:', error);
            
        }
    };

    return (
        <><h2 className='title'>Login</h2>
        <form onSubmit={handleSubmit} className="login-form">
            <input type="text" name="userName" placeholder="User Name" onChange={handleChange} required />
            <input type="password" name="password" placeholder="Password" onChange={handleChange} required />
            {errorMessage && <p className="error-message">{errorMessage}</p>}
            <button type="submit">Login</button>
        </form>
        <p className="register-prompt">
            If you don't have an account, please{' '}
            <Link to="/register" className="register-link">Register</Link>.
        </p>
        </>
    );
};


export default UserLogin