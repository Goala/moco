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

  Device device = Device(WiFi.macAddress(), "NodeMCU");  
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& deviceJSON = jsonBuffer.createObject();
  deviceJSON["available"] = (device.is_available()) ? true : false;
  deviceJSON["name"] = device.get_name();
  deviceJSON["mac"] = device.get_mac();
  

  bool alreadyInBase = false;
  String deviceKey = "";
  
  FirebaseObject child = Firebase.get("devices");
  JsonObject& obj = child.getJsonVariant();

  for (auto kv : obj) {   
    FirebaseObject child2 = Firebase.get("devices/"+ String(kv.key));
    JsonObject& obj2 = child2.getJsonVariant();
    for (auto kv2 : obj2) {
      if(String(kv2.value.as<char*>()).equals(device.get_mac())) {
        alreadyInBase = true;
        deviceKey = String(kv.key);
      }
    }
  }

  if(!alreadyInBase) {
    Firebase.push("devices", deviceJSON);    
    Serial.print("pushed: /device: ");
    Serial.print(device.get_name());
  } else {
    Serial.print("need to update:");
    Serial.println(deviceKey);
  }
  
  delay(1000);
}


void loop() {
  
}
