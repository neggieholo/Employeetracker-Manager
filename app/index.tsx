import "./global.css"
import { useEffect, useState } from 'react';
import { Text, View, StyleSheet, NativeEventEmitter, NativeModules } from 'react-native';
import * as LocationModule from '../modules/LocationModule';


type LocationState = {
  latitude: number;
  longitude: number;
  address?: string | null;
};

export default function Index() {
  const [location, setLocation] = useState<LocationState | null>(null);

  useEffect(() => {
  const subscription = LocationModule.addLocationListener((data) => {
    console.log("New Position:", data.latitude, data.longitude);
    console.log("Address:", data.address);
    
    // Update your state
    setLocation(data);
    
    // Send to your Socket.io backend
    // socket.emit('position_update', data);
  });

  return () => {
    subscription.remove();
  };
}, []);

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Employee Tracker (Manager)</Text>

      {location ? (
        <>
          <Text>Latitude: {location.latitude}</Text>
          <Text>Longitude: {location.longitude}</Text>
          <Text>
            Address: {location.address ?? 'Resolving...'}
          </Text>
        </>
      ) : (
        <Text>Waiting for location…</Text>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center'
  },
  title: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 12
  }
});
