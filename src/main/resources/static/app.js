// API Base URL
const API_BASE = '';

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    addWelcomeMessage();
});

// Add welcome message to chat
function addWelcomeMessage() {
    const chatMessages = document.getElementById('chatMessages');
    const welcomeMsg = document.createElement('div');
    welcomeMsg.className = 'message ai';
    welcomeMsg.innerHTML = `
        <div class="label">🤖 AI Assistant</div>
        <div class="content">Hello! I'm your AI assistant powered by Gemini. Ask me anything!</div>
    `;
    chatMessages.appendChild(welcomeMsg);
}

// Send prompt to AI
async function sendPrompt() {
    const input = document.getElementById('promptInput');
    const prompt = input.value.trim();

    if (!prompt) {
        alert('Please enter a prompt');
        return;
    }

    // Add user message to chat
    addMessage('user', prompt);

    // Clear input
    input.value = '';

    // Show loading state
    setLoading('send', true);

    try {
        const response = await fetch(`${API_BASE}/api/prompt`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ prompt })
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();

        // Add AI response to chat
        addMessage('ai', data.response || 'No response received');

    } catch (error) {
        console.error('Error:', error);
        addMessage('error', `Error: ${error.message}. Please try again.`);
    } finally {
        setLoading('send', false);
    }
}

// Add message to chat
function addMessage(type, content) {
    const chatMessages = document.getElementById('chatMessages');
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${type}`;

    let label = '';

    switch(type) {
        case 'user':
            label = '👤 You';
            break;
        case 'ai':
            label = '🤖 AI Assistant';
            break;
        case 'error':
            label = '⚠️ Error';
            break;
    }

    messageDiv.innerHTML = `
        <div class="label">${label}</div>
        <div class="content">${escapeHtml(content)}</div>
    `;

    chatMessages.appendChild(messageDiv);

    // Scroll to bottom
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

// Ask chatbot with weather context
async function askChatbot() {
    const locationInput = document.getElementById('chatbotLocation');
    const questionInput = document.getElementById('chatbotQuestion');
    const location = locationInput.value.trim();
    const question = questionInput.value.trim();

    if (!location) {
        alert('Please enter a location');
        return;
    }

    if (!question) {
        alert('Please enter a question');
        return;
    }

    const resultDiv = document.getElementById('chatbotResult');
    setLoading('chatbot', true);

    try {
        const response = await fetch(`${API_BASE}/api/chatbot/ask`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                question: question,
                location: location
            })
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();

        // Display the answer
        resultDiv.innerHTML = `
            <div class="chatbot-answer">
                <strong>🤖 AI Answer:</strong>
                ${escapeHtml(data.answer)}
            </div>
        `;

        // Clear inputs
        questionInput.value = '';

    } catch (error) {
        console.error('Error:', error);
        resultDiv.innerHTML = `
            <div class="error-message">
                <strong>Error:</strong> ${error.message}<br>
                Please check your inputs and try again.
            </div>
        `;
    } finally {
        setLoading('chatbot', false);
    }
}

// Get weather data
async function getWeather() {
    const input = document.getElementById('locationInput');
    const location = input.value.trim();

    if (!location) {
        alert('Please enter a location');
        return;
    }

    const resultDiv = document.getElementById('weatherResult');
    setLoading('weather', true);

    try {
        const response = await fetch(`${API_BASE}/weather/current?q=${encodeURIComponent(location)}`);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        displayWeather(data);

    } catch (error) {
        console.error('Error:', error);
        resultDiv.innerHTML = `
            <div class="error-message">
                <strong>Error:</strong> ${error.message}<br>
                Please check the location name and try again.
            </div>
        `;
    } finally {
        setLoading('weather', false);
    }
}

// Display weather data
function displayWeather(data) {
    const resultDiv = document.getElementById('weatherResult');

    const location = data.location;
    const current = data.current;

    resultDiv.innerHTML = `
        <div class="weather-card">
            <div class="weather-header">
                <img src="https:${current.condition.icon}" alt="${current.condition.text}" class="weather-icon">
                <div class="weather-main">
                    <div class="weather-location">${location.name}, ${location.country}</div>
                    <div class="weather-condition">${current.condition.text}</div>
                </div>
            </div>
            
            <div class="weather-temp">${current.temp_c}°C</div>
            
            <div class="weather-details">
                <div class="weather-detail">
                    <div class="weather-detail-label">Feels Like</div>
                    <div class="weather-detail-value">${current.feelslike_c}°C</div>
                </div>
                <div class="weather-detail">
                    <div class="weather-detail-label">Humidity</div>
                    <div class="weather-detail-value">${current.humidity}%</div>
                </div>
                <div class="weather-detail">
                    <div class="weather-detail-label">Wind</div>
                    <div class="weather-detail-value">${current.wind_kph} km/h</div>
                </div>
                <div class="weather-detail">
                    <div class="weather-detail-label">Pressure</div>
                    <div class="weather-detail-value">${current.pressure_mb} mb</div>
                </div>
                <div class="weather-detail">
                    <div class="weather-detail-label">Visibility</div>
                    <div class="weather-detail-value">${current.vis_km} km</div>
                </div>
                <div class="weather-detail">
                    <div class="weather-detail-label">UV Index</div>
                    <div class="weather-detail-value">${current.uv}</div>
                </div>
            </div>
            
            <div style="margin-top: 1rem; text-align: center; opacity: 0.8; font-size: 0.875rem;">
                Last updated: ${current.last_updated}
            </div>
        </div>
    `;
}

// Set loading state
function setLoading(type, isLoading) {
    if (type === 'send') {
        const btn = document.querySelector('.btn-primary');
        const text = document.getElementById('sendBtnText');
        const loading = document.getElementById('sendBtnLoading');

        btn.disabled = isLoading;
        text.style.display = isLoading ? 'none' : 'inline';
        loading.style.display = isLoading ? 'inline-flex' : 'none';
    } else if (type === 'weather') {
        const btn = document.querySelector('.btn-secondary');
        const text = document.getElementById('weatherBtnText');
        const loading = document.getElementById('weatherBtnLoading');

        btn.disabled = isLoading;
        text.style.display = isLoading ? 'none' : 'inline';
        loading.style.display = isLoading ? 'inline-flex' : 'none';
    } else if (type === 'chatbot') {
        const btn = document.querySelector('.btn-success');
        const text = document.getElementById('chatbotBtnText');
        const loading = document.getElementById('chatbotBtnLoading');

        btn.disabled = isLoading;
        text.style.display = isLoading ? 'none' : 'inline';
        loading.style.display = isLoading ? 'inline-flex' : 'none';
    }
}

// Escape HTML to prevent XSS
function escapeHtml(text) {
    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };
    return text.replace(/[&<>"']/g, m => map[m]);
}
