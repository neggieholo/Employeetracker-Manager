import { requireNativeModule, EventEmitter, EventSubscription } from 'expo-modules-core';

// 1. Define the event map so the Emitter knows what data to expect
type LocationEvents = {
  onLocationUpdate: (event: {
    latitude: number;
    longitude: number;
    address: string;
  }) => void;
};

// 2. Load the Native Module
// Ensure the name matches exactly what you put in Kotlin: Name("LocationModule")
const LocationTrackingModule = requireNativeModule('LocationModule');

// 3. Initialize the Emitter with our custom type
const emitter = new EventEmitter<LocationEvents>(LocationTrackingModule);

// 4. Export the functions
export function startTracking(): void {
  return LocationTrackingModule.startTracking();
}

export function stopTracking(): void {
  return LocationTrackingModule.stopTracking();
}

export function addLocationListener(
  listener: (event: { latitude: number; longitude: number; address: string }) => void
): EventSubscription {
  return emitter.addListener('onLocationUpdate', listener);
}

export default LocationTrackingModule;