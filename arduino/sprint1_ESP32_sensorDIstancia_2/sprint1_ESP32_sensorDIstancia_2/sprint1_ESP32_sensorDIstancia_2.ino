#include <NewPing.h>
#include "claseServer.cpp"
#include <sstream>


#define TRIGGER_PIN 12 // Pin TRIGGER del HC-SR04 conectado al pin GPIO12 en ESP32
#define ECHO_PIN 14    // Pin ECHO del HC-SR04 conectado al pin GPIO14 en ESP32
#define CAL_MAX_DISTANCE 1000 // Distancia máxima en centímetros para medir (ajusta según tus necesidades)
#define DISTANCE_THRESHOLD 10 // Umbral para detectar cambios bruscos en la distancia
#define LED_PIN 16 // Pin al que está conectado el LED

NewPing cal_sonar(TRIGGER_PIN, ECHO_PIN, CAL_MAX_DISTANCE);
  
int MAX_DISTANCE = cal_sonar.ping_cm() - (cal_sonar.ping_cm()*0.1);  // Calibrar la distancia maxima para que esté fuera de rango 20cm de la pimera pared

NewPing sonar(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

claseServer servidor;


//long previousDistance = 0;
//int consecutiveChanges;

void setup() {
  Serial.begin(115200);
  pinMode(LED_PIN, OUTPUT); // Configura el pin del LED como salida
  digitalWrite(LED_PIN, HIGH);
  delay(50);
  digitalWrite(LED_PIN, LOW);
  delay(50);
  digitalWrite(LED_PIN, HIGH);
  delay(50);
  digitalWrite(LED_PIN, LOW);
  servidor.declararWifiSSID_Y_Contrasenya("POCO_NSFW", "bromitanomas");
  servidor.declararCanal(1234);
  servidor.setupServer();
    
}


void loop() {
  unsigned int distance = sonar.ping_cm(); // Realiza la medición y obtiene la distancia en centímetros

  Serial.println(MAX_DISTANCE);

  if (distance == 0) {
    Serial.println("Fuera de rango"); // Si la medición está fuera del rango del sensor
        digitalWrite(LED_PIN, LOW); // Apaga el LED

  } else {
    const char* dis = "Distancia: ";
    const char* cm = " cm";
    Serial.print(dis);
    //servidor.enviarStringACliente( dis );

    Serial.print(distance);
    //const char* char_distance;
    //sprintf(char_distance, "%d", distance);

    std::ostringstream oss;
    oss << distance;
    const char* result = oss.str().c_str();
  
    servidor.enviarStringACliente(result);
    Serial.println(cm);

    //servidor.enviarStringACliente(cm);
    
    digitalWrite(LED_PIN, HIGH); // Apaga el LED
    delay(100);
  }

    /*// Detecta un cambio brusco en la distancia
    if (abs(int(distance) - int(previousDistance)) > DISTANCE_THRESHOLD) {
      consecutiveChanges++;
      Serial.print("Cambio brusco detectado. Contador: ");
      Serial.println(consecutiveChanges);
      delay(200);

      if (consecutiveChanges == 1) {
        digitalWrite(LED_PIN, HIGH); // Enciende el LED
        delay(100);

        //consecutiveChanges = 0;
      }
    } else {
      consecutiveChanges = 0;
      digitalWrite(LED_PIN, LOW); // Apaga el LED
    }

    previousDistance = distance;
  }*/

  delay(150); // Espera un poco antes de realizar la siguiente medición
}
