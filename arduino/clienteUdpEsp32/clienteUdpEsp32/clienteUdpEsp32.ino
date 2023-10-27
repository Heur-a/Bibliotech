#include "WiFi.h"
#include "AsyncUDP.h"
#include <M5Stack.h>

#define BLANCO 0xFFFF
#define NEGRO 0
#define ROJO 0xF800
#define VERDE 0x07E0
#define AZUL 0x001F

const char * ssid= "POCO_NSFW";
const char * password= "bromitanomas";

char texto[20];
int hora;
boolean rec = 0;
AsyncUDP udp;

void setup() {
  M5.begin();
  M5.Lcd.setTextSize(2); // Tamaño del texto
  // Serial.begin(115200);

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  if (WiFi.waitForConnectResult() != WL_CONNECTED) {
    Serial.println("WiFi Failed");
    while (1) {
      delay(1000);
    }
  }

  if (udp.listen(1234)) {
    Serial.print("UDP Listening on IP: ");
    Serial.println(WiFi.localIP());

    udp.onPacket([](AsyncUDPPacket packet) {
      int i = 20;
      while (i--) {
        texto[i] = packet.data()[i];
      }
      rec = 1; // Indica mensaje recibido
    });
  }
}

void loop() {
  if (rec) {
    // Enviar broadcast
    rec = 0; // Mensaje procesado
    udp.broadcastTo("Recibido", 1234); // Envia confirmación
    udp.broadcastTo(texto, 1234); // Y dato recibido
    //hora = atol(texto); // Paso de texto a entero
    Serial.println(texto);
    //Serial.println(hora);

    // Mandar a M5Stack
    M5.Lcd.fillScreen(NEGRO); // Borrar pantalla
    M5.Lcd.setCursor(0, 10); // Posición inicial del cursor
    M5.Lcd.setTextColor(BLANCO); // Color del texto
    M5.Lcd.print(texto);
  }
}





