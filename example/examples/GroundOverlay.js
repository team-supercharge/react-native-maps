import React from 'react';
import {
  StyleSheet,
  View,
  Dimensions,
  Text,
  TouchableOpacity,
  Button,
} from 'react-native';

import MapView from 'react-native-maps';
import stripedCircleImg from './assets/stripped_oval.png';

const { width, height } = Dimensions.get('window');

const ASPECT_RATIO = width / height;
const LATITUDE = 37.78825;
const LONGITUDE = -122.4324;
const LATITUDE_DELTA = 0.0322;
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
          latitude: 37.795327,
          longitude: -122.426769,
        },
        {
          latitude: 37.791166,
          longitude: -122.432138,
        },
      ],
      contentText: '33',
      textSize: 32.0,
      textColor: '#ffffff',
      transparency: 0.0, // 0-1
      zIndex: 0,
      isNumberShown: false,
      image: stripedCircleImg,
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
          <MapView.GroundOverlay
            identifier={'111'}
            onPress={() => this.setState({ isNumberShown: !this.state.isNumberShown })}
            northeastCoordinate={this.state.coordinates[0]}
            southwestCoordinate={this.state.coordinates[1]}
            image={this.state.image}
            contentText={this.state.contentText}
            textSize={this.state.textSize}
            textColor={this.state.textColor}
            transparency={this.state.transparency}
            isNumberShown={this.state.isNumberShown}
            zIndex={this.state.zIndex}
          />
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
