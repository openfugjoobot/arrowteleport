# Expo SDK 52 & React Native Best Practices 2025
## Research Report for Tauben Scanner App

**Research Date:** February 28, 2026  
**Target SDK:** Expo SDK 52  
**React Native Version:** 0.76.x  
**Report Type:** Analysis & Recommendations

---

## Executive Summary

Expo SDK 52 (released November 2024) represents a major milestone for React Native development, with the **New Architecture enabled by default** and significant improvements in performance, debugging, and developer experience. This report analyzes the current tauben-scanner codebase against 2025 best practices and provides actionable recommendations.

### Key Highlights
- ✅ New Architecture (Fabric + TurboModules) is now production-ready
- ✅ Expo SDK 52 requires React Native 0.76.x
- ✅ Camera permissions model has changed significantly
- ✅ React Navigation v7 introduces new static configuration API
- ✅ Zustand 5.x with improved TypeScript support
- ⚠️ React Native Vision Camera is now the preferred camera library

---

## 1. Expo SDK 52 Best Practices

### New Architecture (Enabled by Default)

Expo SDK 52 ships with React Native's New Architecture enabled by default:

```javascript
// app.json - Explicitly enable (optional, as it's default)
{
  "expo": {
    "plugins": [
      [
        "expo-build-properties",
        {
          "ios": {
            "newArchEnabled": true
          },
          "android": {
            "newArchEnabled": true
          }
        }
      }
    ]
  }
}
```

**Benefits of New Architecture:**
1. **Fabric Renderer**: Direct Shadow Tree synchronization - no more async bridge
2. **TurboModules**: Lazy-loaded native modules with synchronous execution
3. **JSI (JavaScript Interface)**: Direct memory sharing between JS and native
4. **Yoga 3.0**: Improved layout engine

**Impact on Tauben Scanner:**
- Camera operations will be significantly faster
- Scan processing with native modules will benefit from synchronous execution
- Better memory management for image handling

### Deprecated Features in SDK 52

| Feature | Status | Migration |
|---------|--------|-----------|
| `expo-camera` ~15.x | ❌ Deprecated | Upgrade to ^16.0.0 |
| `classic` build system | ⚠️ Deprecated | Use EAS Build |
| `react-native-webview` | ✅ Still supported | No changes needed |
| `Permissions.askAsync` | ❌ Removed | Use module-specific permission hooks |

### Recommended SDK 52 Patterns

**Permission Pattern (NEW):**
```typescript
// ✅ CORRECT: Use module-specific hooks
import { useCameraPermissions } from 'expo-camera';

const [permission, requestPermission] = useCameraPermissions();

// ❌ OLD: Using Permissions API directly
import { Permissions } from 'expo';
const { status } = await Permissions.askAsync(Permissions.CAMERA);
```

---

## 2. React Native Performance (2025)

### Latest Optimization Techniques

**1. useMemo/useCallback for NativeProps**
```typescript
// ✅ CORRECT: Memoize props passed to native components
const cameraProps = useMemo(() => ({
  style: styles.camera,
  type: deviceType,
  flashMode: flashMode,
}), [deviceType, flashMode]);

<CameraView {...cameraProps} />
```

**2. FlashList vs FlatList (2025 Update)**
```typescript
// FlashList is now the community standard
import { FlashList } from '@shopify/flash-list';

<FlashList
  data={pigeons}
  renderItem={renderPigeonItem}
  estimatedItemSize={100} // Required for performance
  onEndReached={loadMore}
/>
```

**3. Hermes Engine Optimizations (2025)**
- Default in RN 0.76
- Improved garbage collection
- Smaller bundle sizes
- Better debugging with Flipper successor

### Re-Rendering Prevention

**1. Zustand Selector Pattern:**
```typescript
// ✅ CORRECT: Select specific slices
const isLoading = useScanStore((state) => state.isLoading);

// ❌ WRONG: Destructuring causes re-renders on any store change
const { isLoading, scanResult } = useScanStore();
```

**2. React.memo with Custom Comparison:**
```typescript
const PigeonCard = React.memo(({ pigeon, onPress }) => {
  // ...
}, (prev, next) => {
  return prev.pigeon.id === next.pigeon.id &&
         prev.pigeon.updatedAt === next.pigeon.updatedAt;
});
```

**3. InteractionManager for Heavy Operations:**
```typescript
import { InteractionManager } from 'react-native';

const handleScan = async () => {
  setIsScanning(true);
  
  // Defer heavy processing until after transition
  const taskHandle = InteractionManager.runAfterInteractions(async () => {
    const result = await processScan(image);
    setScanResult(result);
  });
  
  return () => taskHandle.cancel();
};
```

---

## 3. Camera & Image Handling

### React Native Vision Camera vs Expo Camera

**2025 Recommendation: Vision Camera is NOW PREFERRED**

| Feature | Expo Camera 16.x | Vision Camera 4.x |
|---------|-------------------|-------------------|
| New Architecture | ✅ Supported | ✅ Native support |
| Frame Processor | ⚠️ Limited | ✅ Full support |
| Plugin Ecosystem | ❌ Limited | ✅ 15+ plugins (QR, OCR, ML) |
| Performance | ⚠️ Good | ✅ Better (direct native) |
| Maintenance | ⚠️ Expo team | ✅ Active community |
| EAS Build | ✅ Built-in | ✅ Compatible |
| Code Scanner | ✅ Built-in | ✅ Built-in |

**For Tauben Scanner:**
Vision Camera's frame processor would allow real-time image analysis (e.g., bird detection) before capture - a significant UX improvement.

### Recommended Implementation

```typescript
// Using Vision Camera (Recommended for SDK 52+)
import { Camera, useCameraDevice } from 'react-native-vision-camera';

const device = useCameraDevice('back');
const { hasPermission, requestPermission } = useCameraPermission();

// Frame processor for real-time analysis
const frameProcessor = useFrameProcessor((frame) => {
  'worklet';
  // Run in separate thread - no JS bridge blocking
  const faces = detectFaces(frame);
  // Could detect pigeons/birds in frame
}, []);

<Camera
  device={device}
  isActive={isActive}
  frameProcessor={frameProcessor}
  photo={true}
/>
```

### Image Handling Best Practices

**1. expo-image over react-native Image:**
```typescript
import { Image } from 'expo-image';

// Supports:
// - WebP/AVIF
// - Cache control
// - Content placeholders
// - Blur hash
<Image
  source={{ uri: pigeon.photoUrl }}
  contentFit='cover'
  placeholder={require('./placeholder.png')}
  cachePolicy='memory-disk'
  transition={200}
/>
```

**2. Image Processing Pipeline:**
```typescript
import * as ImageManipulator from 'expo-image-manipulator';

const processImage = async (uri: string) => {
  // Resize before upload
  const manipulated = await ImageManipulator.manipulateAsync(
    uri,
    [{ resize: { width: 1024 } }], // Limit max dimension
    { compress: 0.8, format: ImageManipulator.SaveFormat.JPEG }
  );
  
  // Create thumbnail
  const thumbnail = await ImageManipulator.manipulateAsync(
    manipulated.uri,
    [{ resize: { width: 200 } }],
    { compress: 0.7, format: ImageManipulator.SaveFormat.JPEG }
  );
  
  return { original: manipulated, thumbnail };
};
```

---

## 4. Navigation v7 Patterns

### React Navigation 7 Major Changes

**Released:** October 2024  
**Minimum Requirements:**
- React Native >= 0.72.0
- Expo >= 52
- TypeScript >= 5.0.0 (if using TS)

### Breaking Changes (Relevant to Tauben)

**1. Navigate Action Changes:**
```typescript
// ❌ OLD: Navigate would go back if screen exists
navigation.navigate('Details', { id: 1 });

// ✅ NEW: navigate always pushes, popTo goes back
navigation.push('Details', { id: 1 }); // Always push
navigation.popTo('Details', { id: 1 }); // Go back to existing
```

**2. Static Configuration API (NEW FEATURE):**
```typescript
// ✅ PREFERRED: Static config with type inference
const RootStack = createNativeStackNavigator({
  screens: {
    Home: HomeScreen,
    Details: {
      screen: DetailsScreen,
      options: { title: 'Details' },
      linking: 'details/:id', // Inline deep linking
    },
  },
});

// Automatic type generation - no separate ParamList needed!
```

**3. Layout Props:**
```typescript
// Add error boundaries at screen level
<Stack.Screen
  name="Scan"
  component={ScanScreen}
  layout={({ children }) => (
    <ErrorBoundary>
      <Suspense fallback={<Spinner />}>
        {children}
      </Suspense>
    </ErrorBoundary>
  )}
/>
```

**4. Preloading Screens:**
```typescript
// New feature: Preload screens before navigation
const handlePigeonPress = (pigeonId: string) => {
  // Preload while user decides to navigate
  navigation.preload('PigeonDetails', { id: pigeonId });
};

const navigateToDetails = () => {
  navigation.navigate('PigeonDetails', { id: pigeonId });
};
```

### Navigation v7 Best Practices

**Bottom Tabs with Side Position Support (NEW):**
```typescript
<Tab.Navigator
  screenOptions={{
    tabBarPosition: 'left', // NEW: 'left', 'top', or 'bottom'
    animation: 'fade',      // NEW: Tab animations
  }}
>
```

**Bottom Tabs Animation:**
```typescript
// Native-like tab transitions
<Tab.Navigator
  screenOptions={{
    animation: 'shift', // 'fade', 'shift', or 'none'
  }}
>
```

---

## 5. State Management 2025

### Zustand vs Redux Toolkit vs Jotai

| Feature | Zustand 5.x | Redux Toolkit 2.x | Jotai 2.x |
|---------|-------------|-------------------|-----------|
| Boilerplate | ✅ Minimal | ⚠️ Moderate | ✅ Minimal |
| DevTools | ✅ Built-in | ✅ Excellent | ✅ Good |
| Async State | ⚠️ Manual | ✅ RTK Query built-in | ⚠️ Manual |
| Re-renders | ✅ Optimized | ⚠️ Connected components | ✅ Atomic |
| Learning Curve | ✅ Low | ⚠️ Higher | ✅ Low |
| Expo SDK 52 | ✅ Full support | ✅ Supported | ✅ Supported |
| Bundle Size | ✅ Tiny (~1KB) | ⚠️ Larger (~10KB) | ✅ Tiny |
| TypeScript | ✅ Excellent | ✅ Excellent | ⚠️ Good |

**2025 Recommendation: Zustand 5.x for Tauben Scanner**

Reasons:
1. Proven in current tauben codebase
2. Minimal boilerplate matches project scale
3. Redux Toolkit is overkill for this app size
4. Excellent TypeScript inference in v5

### Zustand 5.x Best Practices

**1. Store Slicing Pattern:**
```typescript
// stores/settingsStore.ts
import { create } from 'zustand';
import { persist, createJSONStorage } from 'zustand/middleware';
import { storage } from '../utils/storage';

interface SettingsState {
  apiUrl: string;
  apiKey: string | null;
  notificationsEnabled: boolean;
  setApiUrl: (url: string) => void;
  setApiKey: (key: string | null) => void;
  toggleNotifications: () => void;
}

export const useSettingsStore = create<SettingsState>()(
  persist(
    (set) => ({
      apiUrl: 'https://api.tauben-scanner.de',
      apiKey: null,
      notificationsEnabled: true,
      setApiUrl: (url) => set({ apiUrl }),
      setApiKey: (key) => set({ apiKey }),
      toggleNotifications: () => set((state) => ({
        notificationsEnabled: !state.notificationsEnabled,
      })),
    }),
    {
      name: 'settings-storage',
      storage: createJSONStorage(() => storage),
    }
  )
);
```

**2. React Query + Zustand Integration:**
```typescript
// hooks/usePigeonQueries.ts
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useApiClient } from './useApiClient';

export const usePigeons = () => {
  const apiClient = useApiClient();
  
  return useQuery({
    queryKey: ['pigeons'],
    queryFn: () => apiClient.get('/pigeons'),
    staleTime: 5 * 60 * 1000, // 5 minutes
  });
};

export const useCreateSighting = () => {
  const queryClient = useQueryClient();
  const apiClient = useApiClient();
  
  return useMutation({
    mutationFn: (data: SightingInput) => 
      apiClient.post('/sightings', data),
    onSuccess: () => {
      // Invalidate and refetch
      queryClient.invalidateQueries({ queryKey: ['pigeons'] });
    },
  });
};
```

**3. Store Selection Patterns:**
```typescript
// ✅ CORRECT: Select only what you need
const ScanScreen = () => {
  const isScanning = useScanStore((state) => state.isScanning);
  const capturePhoto = useScanStore((state) => state.capturePhoto);
  
  // Only re-renders when isScanning changes
};

// ❌ WRONG: Causes unnecessary re-renders
const ScanScreen = () => {
  const { isScanning, photo, scanResult, error } = useScanStore();
  
  // Re-renders on ANY store change
};
```

### Server State vs Client State (2025 Decision Tree)

**Use React Query for:**
- API data fetchi