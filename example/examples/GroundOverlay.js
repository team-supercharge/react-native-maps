import React from 'react';
import {
  StyleSheet,
  View,
  Dimensions,
} from 'react-native';
import MapView from 'react-native-maps';

const { width, height } = Dimensions.get('window');

const ASPECT_RATIO = width / height;
const LATITUDE = 37.78825;
const LONGITUDE = -122.4324;
const LATITUDE_DELTA = 0.0922;
const LONGITUDE_DELTA = LATITUDE_DELTA * ASPECT_RATIO;

class GroundOverlay extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      region: {
        latitude: LATITUDE,
        longitude: LONGITUDE,
        latitudeDelta: LATITUDE_DELTA,
        longitudeDelta: LONGITUDE_DELTA,
      },
      coordinate: {
        longitude: -74.22655,
        latitude: 40.712216,
      },
      image: 'flag-blue',
    };
  }

  render() {
    return (
      <View style={styles.container}>
        <MapView
          provider={this.props.provider}
          style={styles.map}
          rotateEnabled={false}
          initialRegion={this.state.region}
        >
          <MapView.GroundOverlay coordinate={this.state.coordinate} image={this.state.image} />
        </MapView>
      </View>
    );
  }
}

GroundOverlay.propTypes = {
  provider: MapView.ProviderPropType,
};

const styles = StyleSheet.create({
  container: {
    ...StyleSheet.absoluteFillObject,
    justifyContent: 'flex-end',
    alignItems: 'center',
  },
  scrollview: {
    alignItems: 'center',
    paddingVertical: 40,
  },
  map: {
    ...StyleSheet.absoluteFillObject,
  },
});

module.exports = GroundOverlay;
