import React,{useState} from 'react'
import '../userRegistration.css'
import axios from 'axios';
import { useNavigate,Link } from 'react-router-dom';

const API_BASE_URL = 'http://localhost:8080';

const UserRegistration = () => {

    const navigate = useNavigate()

    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        userName: '',
        email: '',
        password: '',
        phoneNumber: '',
        gender: ''
    });

    const [errorMessage, setErrorMessage] = useState('')

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {

            const response = await axios.post(`${API_BASE_URL}/signup`, formData)

            if(response.status === 200)
                console.log('Registration successful:', response);

            navigate('/login')

        } catch (error) {
            if(error.response && error.response.status === 401)
                setErrorMessage('user name already exist')
            else
                setErrorMessage('error occured, please try again later')

            console.error('Registration failed:', error);
        }
    };

    return (
        <><h2 className='title'>Registration</h2>
        <form onSubmit={handleSubmit} className="registration-form">
            <input type="text" name="firstName" placeholder="First Name" onChange={handleChange} required />
            <input type="text" name="lastName" placeholder="Last Name" onChange={handleChange} required />
            <input type="text" name="userName" placeholder="User Name" onChange={handleChange} required />
            {errorMessage && <p className="error-message">{errorMessage}</p>}
            <input type="email" name="email" placeholder="Email" onChange={handleChange} />
            <input type="password" name="password" placeholder="Password" onChange={handleChange} required />
            <input type="text" name="phoneNumber" placeholder="Phone Number" onChange={handleChange} required />
            <div className="gender-selection">
                <label>
                    <input type="radio" name="gender" value="Male" onChange={handleChange} required />
                    Male
                </label>
                <label>
                    <input type="radio" name="gender" value="Female" onChange={handleChange} required />
                    Female
                </label>
                <label>
                    <input type="radio" name="gender" value="Other" onChange={handleChange} required />
                    Other
                </label>
            </div>
            <button type="submit">Register</button>
        </form>
        <p className="login-prompt">
            If you have an account, please{' '}
            <Link to="/login" className="register-link">Login</Link>.
        </p>
        </>
    );
};


export default UserRegistration