import React from 'react';
import {
  StyleSheet,
  View,
  Dimensions,
  Text,
  TouchableOpacity,
} from 'react-native';

import MapView from 'react-native-maps';
import stripedCircleImg from './assets/stripped_oval.png';

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
      coordinates: [
              {
                latitude: 47.495722,
                longitude: 19.058050,
              },
              {
                              latitude: 47.494236,
                              longitude: 19.055797,
                            },
            ],
    };
  }

  render() {
    return (
      <View style={styles.container}>
        <MapView
          provider={this.props.provider}
          style={styles.map}
          rotateEnabled={false}
          initialRegion={this.state.region}>
          <MapView.GroundOverlay
            northeastCoordinate={this.state.coordinates[0]}
            southwestCoordinate={this.state.coordinates[1]}
            image={stripedCircleImg} />
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
