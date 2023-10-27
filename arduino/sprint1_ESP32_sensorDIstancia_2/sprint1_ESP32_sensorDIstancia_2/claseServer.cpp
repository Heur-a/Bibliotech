
#include <WiFi.h> 
#include <AsyncUDP.h> 
#include <TimeLib.h> 
#include <string>
#include <Arduino.h>

//#include "claseServer.h"


using namespace std;


class claseServer {

    private:
        char* ssid;
        char* password;
        
        int canal;  

        AsyncUDP udp;

        


    public:

        void declararWifiSSID_Y_Contrasenya( char* nueva_ssid, char* nueva_password ){
            ssid = nueva_ssid;
            password = nueva_password;
        }

        void declararCanal (int nuevo_canal) {
            canal = nuevo_canal;
        }

        void setupServer() { 
          

          //setTime(10, 0, 0, 7, 10, 2018); 
          //hora minuto segundo dia mes año 
  
          WiFi.mode(WIFI_STA); 
          WiFi.begin(ssid, password); 
          if(WiFi.waitForConnectResult() != WL_CONNECTED) { 
              Serial.println("WiFiFailed"); 
                while(1) { 
                delay(1000); 
              } 
          }

          if(udp.listen(canal)) { 
              Serial.print("UDP ListeningonIP: ");
              Serial.println(WiFi.localIP());
              udp.onPacket([](AsyncUDPPacket packet) { 
                  Serial.write(packet.data(), packet.length());
                  Serial.println(); 
              }); 
          }

        }

        /*void enviarIntACliente(int data) {
          udp.broadcastTo(&data, sizeof(data), canal);
        }

        void enviarDoubleACliente(double data) {
            udp.broadcastTo(&data, sizeof(data), canal);
        }*/

        void enviarStringACliente( const char* data ){
         //AsyncUDP udp;
          //char texto[] = data;
          udp.broadcastTo(data , canal);
        }

        /*void enviarListOfIntACliente( int[] data ){
            udp.broadcastTo(data , canal);
        }

        void enviarListOfDoubleACliente( double[] data ){
            u.broadcastTo(data , canal);
        }

        /*void enviarListOfStringACliente( string[] data ){
            upd.broadcastTo(data , canal);
        }*/   

};