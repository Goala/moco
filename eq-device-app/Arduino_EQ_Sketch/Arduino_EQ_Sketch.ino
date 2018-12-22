#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

// Set these to run example.
#define FIREBASE_HOST "electric-quizer.firebaseio.com"
#define FIREBASE_AUTH "get_your_auth"
#define WIFI_SSID "iMäks"
#define WIFI_PASSWORD "get_your_pass"

class Device {
  private:
    boolean available = true;
    String mac;
    String name;
  public:
    Device(String mac, String name) {
      this->mac = mac;
      this->name = name;
    }
    String get_name() {return (name);}
    String get_mac() {return (mac);}
    boolean is_available() {return (available);}
};



void setup() {
  Serial.begin(9600);

  // connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

  Device device = Device(WiFi.macAddress(),"Ich bin NodeMCU");  
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& deviceJSON = jsonBuffer.createObject();
  deviceJSON["available"] = device.is_available();
  deviceJSON["name"] = device.get_name();
  deviceJSON["mac"] = device.get_mac();
  
  String name = Firebase.push("devices", deviceJSON);
  // handle error
  if (Firebase.failed()) {
      Serial.print("pushing /logs failed:");
      Serial.println(Firebase.error());  
      return;
  }
  
  Serial.print("pushed: /logs/");
  Serial.println(name);
  
  delay(1000);
}

int n = 0;

void loop() {
  
}
