import React, { useState } from 'react';
import ApiService from '../../service/ApiService';
import { useNavigate } from 'react-router-dom';

function RegisterPage() {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password: '',
        phoneNumber: ''
    });

    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleNameInput = (e) => {
        const { value } = e.target;
        if (/^[A-Za-z\s]*$/.test(value)) {
            setFormData({ ...formData, name: value });
        }
    };

    const handlePhoneNumberInput = (e) => {
        const { value } = e.target;
        if (/^\d*$/.test(value)) {
            setFormData({ ...formData, phoneNumber: value });
        }
    };

    const validateForm = () => {
        const { name, email, password, phoneNumber } = formData;

        const namePattern = /^[A-Za-z\s]+$/; 
        const emailPattern = /^[a-z0-9._%+-]+@gmail\.com$/;
        const phonePattern = /^\d{10}$/;
        // const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/; // Minimum 8 characters, at least one letter and one number

        if (!name || !namePattern.test(name)) {
            setErrorMessage('Name must contain only letters and spaces.');
            return false;
        }
        if (!emailPattern.test(email)) {
            setErrorMessage('Please enter a valid Gmail address.');
            return false;
        }
        if (!phonePattern.test(phoneNumber)) {
            setErrorMessage('Please enter a valid 10-digit phone number.');
            return false;
        }
        // if (!passwordPattern.test(password)) {
        //     setErrorMessage('Password must be at least 8 characters long and include at least one letter and one number.');
        //     return false;
        // }

        return true;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!validateForm()) {
            setTimeout(() => setErrorMessage(''), 5000);
            return;
        }
        try {
            // Call the register method from ApiService
            const response = await ApiService.registerUser(formData);

            // Check if the response is successful
            if (response.statusCode === 200) {
                // Clear the form fields after successful registration
                setFormData({
                    name: '',
                    email: '',
                    password: '',
                    phoneNumber: ''
                });
                setSuccessMessage('User registered successfully');
                setTimeout(() => {
                    setSuccessMessage('');
                    navigate('/login');
                }, 3000);
            }
        } catch (error) {
            setErrorMessage(error.response?.data?.message || error.message);
            setTimeout(() => setErrorMessage(''), 5000);
        }
    };

    return (
        <div className="auth-container">
            {errorMessage && <p className="error-message">{errorMessage}</p>}
            {successMessage && <p className="success-message">{successMessage}</p>}
            <h2>Sign Up</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>*Name:</label>
                    <input
                        type="text"
                        name="name"
                        value={formData.name}
                        onChange={handleNameInput}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>*Email:</label>
                    <input
                        type="email"
                        name="email"
                        value={formData.email}
                        onChange={(e) => setFormData({ ...formData, email: e.target.value.toLowerCase() })}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>*Phone Number:</label>
                    <input
                        type="text"
                        name="phoneNumber"
                        value={formData.phoneNumber}
                        onChange={handlePhoneNumberInput}
                        required
                        pattern="\d*"
                        title="Please enter a valid phone number"
                    />
                </div>
                <div className="form-group">
                    <label>*Password:</label>
                    <input
                        type="password"
                        name="password"
                        value={formData.password}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <button type="submit">Register</button>
            </form>
            <p className="register-link">
                Already have an account? <a href="/login">Login</a>
            </p>
        </div>
    );
}

export default RegisterPage;