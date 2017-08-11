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
const LATITUDE = 47.495722;
const LONGITUDE = 19.058050;
const LATITUDE_DELTA = 0.0922;
const LONGITUDE_DELTA = LATITUDE_DELTA * ASPECT_RATIO;

function log(eventName, e) {
  console.log(eventName, e.nativeEvent);
}

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
      contentText: '33',
      textSize: 32.0,
      textColorRed: 204.0, // 0-255
      textColorGreen: 161.0, // 0-255
      textColorBlue: 12.0, // 0-255
      textColorAlpha: 255.0, // 0-255
      transparency: 0.0, // 0-1
      zIndex: 0,
      isNumberShown: false,
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
            onPress={(e) => log('onSelect', e)}

            northeastCoordinate={this.state.coordinates[0]}
                        southwestCoordinate={this.state.coordinates[1]}
                        image={stripedCircleImg}
            contentText={this.state.contentText}
            textSize={40.0}
            textColor={'#0000ff'}
            transparency={this.state.transparency}
            isNumberShown={this.state.isNumberShown}
            zIndex={0}
          />
        </MapView>
        <Button title="change opacity" onPress={() =>
          this.setState({transparency: 0.5})
        } />
        <Button title="set text" onPress={() =>
          this.setState({isNumberShown: true})
        } />
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
