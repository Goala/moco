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
    String get_name() {
      return (name);
    }
    String get_mac() {
      return (mac);
    }
    boolean is_available() {
      return (available);
    }
};

void(* resetFunc) (void) = 0; //declare reset function @ address 0

void setup() {
  Serial.begin(9600);
  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, HIGH);


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

  delay(1000);
}

bool deviceInit = false;
String gameRef = "";
bool alreadyInBase = false;
String deviceKey = "";
bool loadedDevices = false;

void loop() {
  Serial.println(deviceInit);
  if (!deviceInit) {
    digitalWrite(LED_BUILTIN, HIGH);
    Device device = Device(WiFi.macAddress(), DEVICE_NAME);
    StaticJsonBuffer<200> jsonBuffer;
    JsonObject& deviceJSON = jsonBuffer.createObject();
    deviceJSON["available"] = (device.is_available()) ? true : false;
    deviceJSON["name"] = device.get_name();
    deviceJSON["mac"] = device.get_mac();
    FirebaseObject child = Firebase.get("devices");
    if (child.success()) {
      Serial.println("get devices check");
      Serial.flush();
      JsonObject& obj = child.getJsonVariant();
      for (auto kv : obj) {
        FirebaseObject child2 = Firebase.get("devices/" + String(kv.key));
        if (Firebase.failed()) {
          Serial.println(Firebase.error());
        }
        JsonObject& obj2 = child2.getJsonVariant();
        if (child2.success()) {
          Serial.println("get device check");
          Serial.flush();
          loadedDevices = true;
          if (obj2.get<String>("mac").equals(device.get_mac())) {
            alreadyInBase = true;
            deviceKey = String(kv.key);
          }
          deviceInit = true;
          digitalWrite(LED_BUILTIN, LOW);
        } else {
          Serial.println("get device failed");
          resetFunc();
        }
      }
      if (!alreadyInBase && loadedDevices) {
        Firebase.push("devices", deviceJSON);
        Serial.print("pushed: /device: ");
        Serial.println(device.get_name());
        Serial.flush();
      } else {
        if (loadedDevices) {
          String devicePath = "devices/" + deviceKey + "/available";
          Serial.print("need to update: ");
          Serial.println(devicePath);
          Serial.flush();
          Firebase.setBool(devicePath, true);
        } else {
          Serial.println("failed loading");
          resetFunc();
        }
      }
    } else {
      Serial.println("get devices failed");
      resetFunc();
    }
  } else {
    digitalWrite(LED_BUILTIN, LOW);
  }

  delay(5000);
  
  if (gameRef.length() < 1) {
    FirebaseObject foGames = Firebase.get("devices");
    if (Firebase.failed()) {
      Serial.println(Firebase.error());
    }
    if (foGames.success()) {
      Serial.println("get games check");
      Serial.flush();

      JsonObject& games = foGames.getJsonVariant();

      for (auto kv : games) {
        Serial.println(kv.key);
        Serial.println(kv.value.as<char*>());
        Serial.flush();
      }

    } else {
      Serial.println("määähhhh");
    }

  } else {
    Serial.println("schinken");
  }
}
