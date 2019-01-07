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
bool streamSet = false;

void loop() {
  delay(200);
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
        loadedDevices = true;
        JsonObject& value = kv.value;
        if (value.get<String>("mac").equals(device.get_mac())) {
          alreadyInBase = true;
          deviceKey = String(kv.key);
        }
      }
      if (!alreadyInBase && loadedDevices) {
        Firebase.push("devices", deviceJSON);
        Serial.print("pushed: /device: ");
        Serial.println(device.get_name());
        Serial.flush();
        deviceInit = true;
      } else {
        if (loadedDevices) {
          String devicePath = "devices/" + deviceKey + "/available";
          Serial.print("need to update: ");
          Serial.println(devicePath);
          Serial.flush();
          Firebase.setBool(devicePath, true);
          deviceInit = true;
        } else {
          Serial.println("failed loading");
          //resetFunc();
        }
      }
    } else {
      Serial.println("get devices failed");
      //resetFunc();
    }
  } else {
    digitalWrite(LED_BUILTIN, LOW);
  }


  if (gameRef.length() < 1) {
    FirebaseObject foGames = Firebase.get("games");
    if (Firebase.failed()) {
      Serial.println(Firebase.error());
    }
    if (foGames.success()) {
      Serial.println("get games check");
      Serial.flush();

      JsonObject& games = foGames.getJsonVariant();

      for (auto kv : games) {
        JsonObject& value = kv.value;
        if (value.get<String>("deviceId").equals(DEVICE_NAME)) {
          gameRef = "games/" + String(kv.key);
          Serial.print("Node is linked to: ");
          Serial.println(value.get<String>("name"));
        }

      }

    } else {
      Serial.println("get games failed");
      Serial.flush();
    }

  } else {
    if (!streamSet) {
      Serial.println(gameRef);
      Serial.flush();
      Firebase.stream("/test");
      streamSet = true;
    } else {
      if (Firebase.available()) {
        FirebaseObject event = Firebase.readEvent();
        String eventType = event.getString("type");
        eventType.toLowerCase();
        if (eventType == "put") {
          FirebaseObject obj = Firebase.get("test");
          Serial.print("value changed:");
          Serial.println(obj.getString("type"));
        }


        Serial.flush();
      }
    }


  }
}
