#include <FirebaseArduino.h> 
#include <ESP8266WiFi.h>             //wifi library
#define WIFI_SSID "SSID"             //replace SSID with your wifi username
#define WIFI_PASSWORD "PWD"          //replace PWD with your wifi password
#define WIFI_LED D5                  //connect a led to any of the gpio pins of the board and replace pin_number with it eg. D4                      

#define FIREBASE_HOST "x"           //link of api
#define FIREBASE_AUTH "x"           //database secret

void setup() {
  Serial.begin(9600);
  pinMode(WIFI_LED,OUTPUT);                         //define pinmodes
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);  
  //connect to wifi
  while (WiFi.status() != WL_CONNECTED) {           //wait till connected to WiFi
    delay(100);  
    digitalWrite(WIFI_LED,LOW);                     //Blink the light till connected to WiFi
    delay(100);
    digitalWrite(WIFI_LED,HIGH);
    Serial.print("."); }
    
  Serial.println("");
  Serial.println("WiFi connected");
  digitalWrite(WIFI_LED,HIGH);  
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);             //connect to Database
  delay(1000);
}

void loop() {
  String firebaseResult = firebaseGet();
  delay(100);
  if (firebaseResult=="ON"){
      //code to happen if the status is ON  
  }else{
      //code to happen if the status is OFF
    }
} 
