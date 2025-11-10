# AI & Weather Assistant - Spring Boot WebFlux Application

A reactive Spring Boot application that integrates with Google Gemini AI and WeatherAPI to provide an interactive chat interface and real-time weather information.

## Features

✅ **Backend (Spring Boot + WebFlux)**
- Reactive REST API using Spring WebFlux
- WebClient integration with Google Gemini AI API
- WebClient integration with WeatherAPI.com
- Non-blocking, reactive programming model
- Proper error handling and validation

✅ **Frontend**
- Modern, responsive single-page application
- Real-time AI chat interface
- Weather lookup with detailed information
- Beautiful gradient UI with animations
- Mobile-friendly design

## Technologies Used

### Backend
- **Spring Boot 3.5.7**
- **Spring WebFlux** (Reactive web framework)
- **WebClient** (Reactive HTTP client)
- **Java 17**
- **Maven**

### Frontend
- **HTML5**
- **CSS3** (with animations and modern design)
- **Vanilla JavaScript** (Fetch API)

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Google Gemini API Key (free at https://ai.google.dev/)
- WeatherAPI Key (free at https://www.weatherapi.com/)

## Setup Instructions

### 1. Clone or Download the Project

### 2. Get API Keys

#### Google Gemini API Key:
1. Visit https://ai.google.dev/
2. Click "Get API Key"
3. Create a new project or use existing one
4. Copy your API key

#### WeatherAPI Key:
1. Visit https://www.weatherapi.com/
2. Sign up for a free account
3. Copy your API key from the dashboard

### 3. Configure Application Properties

Open `src/main/resources/application.properties` and update:

```properties
# Gemini API Configuration
GEMINI_API_KEY=YOUR_GEMINI_API_KEY_HERE
GEMINI_API_BASEURL=https://generativelanguage.googleapis.com/v1beta/models

# Weather API Configuration
EXT_API_KEY=YOUR_WEATHER_API_KEY_HERE
EXT_API_BASEURL=https://api.weatherapi.com/v1

# Server Configuration
server.port=8080
```

**Important:** Replace `YOUR_GEMINI_API_KEY_HERE` and `YOUR_WEATHER_API_KEY_HERE` with your actual API keys.

### 4. Build and Run

#### Using Maven Wrapper (Recommended):

**Windows:**
```bash
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
./mvnw clean install
./mvnw spring-boot:run
```

#### Using Maven:
```bash
mvn clean install
mvn spring-boot:run
```

### 5. Access the Application

Once the application starts, open your browser and navigate to:
```
http://localhost:8080
```

You should see the AI & Weather Assistant interface.

## API Endpoints

### AI Chat Endpoint
- **URL:** `POST /api/prompt`
- **Request Body:**
  ```json
  {
    "prompt": "Your question here"
  }
  ```
- **Response:**
  ```json
  {
    "response": "AI response text"
  }
  ```

### Weather Endpoint
- **URL:** `GET /weather/current?q={location}`
- **Query Parameters:**
  - `q`: City name (e.g., "Copenhagen", "London") or coordinates (e.g., "48.8567,2.3508")
- **Response:** JSON object with weather data

## Project Structure

```
src/
├── main/
│   ├── java/dk/ss/projectai/
│   │   ├── ProjectAiApplication.java          # Main application class
│   │   └── app/
│   │       ├── client/
│   │       │   └── GeminiClient.java          # Gemini API WebClient
│   │       ├── config/
│   │       │   └── WebClientConfig.java       # WebClient configuration
│   │       ├── controller/
│   │       │   ├── PromptController.java      # AI chat REST controller
│   │       │   └── WeatherController.java     # Weather REST controller
│   │       └── service/
│   │           ├── GeminiService.java         # AI service logic
│   │           └── WeatherService.java        # Weather service logic
│   └── resources/
│       ├── application.properties              # Configuration
│       └── static/                             # Frontend files
│           ├── index.html                      # Main HTML page
│           ├── styles.css                      # Styling
│           └── app.js                          # JavaScript logic
```

## How to Use

### AI Chat
1. Type your question in the text area
2. Click "Send" or press `Ctrl+Enter`
3. Wait for the AI response to appear in the chat
4. Continue the conversation as needed

### Weather Lookup
1. Enter a city name (e.g., "Copenhagen", "Tokyo", "New York")
2. Click "Get Weather" or press `Enter`
3. View detailed weather information including:
   - Temperature (current and feels like)
   - Weather condition with icon
   - Humidity, wind speed, pressure
   - Visibility and UV index

## Testing the APIs

You can test the backend APIs directly using curl or Postman:

### Test AI Endpoint:
```bash
curl -X POST http://localhost:8080/api/prompt \
  -H "Content-Type: application/json" \
  -d '{"prompt":"What is Spring WebFlux?"}'
```

### Test Weather Endpoint:
```bash
curl "http://localhost:8080/weather/current?q=Copenhagen"
```

## Troubleshooting

### Common Issues:

1. **"API key must be provided" error:**
   - Ensure you've added your API keys to `application.properties`
   - Check there are no extra spaces in the keys

2. **"401 Unauthorized" error:**
   - Verify your API keys are valid
   - Check if you have API quota remaining

3. **Port 8080 already in use:**
   - Change the port in `application.properties`: `server.port=8081`

4. **Connection timeout:**
   - Check your internet connection
   - Verify the API base URLs are correct

## Features Implemented

✅ **Requirement 1:** Backend built with Spring Boot and WebFlux (Reactive web) ✓  
✅ **Requirement 2:** WebClient to call external AI service (Gemini API) ✓  
✅ **Requirement 3:** WebClient to call 3rd party API (WeatherAPI) ✓  
✅ **Requirement 4:** Frontend that communicates with backend and allows user to enter prompts ✓

## Additional Features

- Responsive design (mobile, tablet, desktop)
- Loading states and animations
- Error handling with user-friendly messages
- Chat history in the UI
- Beautiful gradient design
- Keyboard shortcuts (Ctrl+Enter to send, Enter for weather)

## Future Enhancements

- Chat history persistence (database)
- User authentication
- Multiple AI model selection
- Weather forecast (7-day)
- Export chat history
- Dark mode toggle

## License

This project is for educational purposes.

## Credits

- Google Gemini AI
- WeatherAPI.com
- Spring Framework Team

