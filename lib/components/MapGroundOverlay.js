import PropTypes from 'prop-types';
import React from 'react';
import {
  StyleSheet,
  Platform,
  NativeModules,
  Animated,
  findNodeHandle,
  ViewPropTypes,
} from 'react-native';

import resolveAssetSource from 'react-native/Libraries/Image/resolveAssetSource';
import decorateMapComponent, {
  SUPPORTED,
  USES_DEFAULT_IMPLEMENTATION,
} from './decorateMapComponent';

const viewConfig = {
  uiViewClassName: 'AIR<provider>MapGroundOverlay',
  validAttributes: {
    coordinate: true,
  },
};

const propTypes = {
  ...ViewPropTypes,

  onPress: PropTypes.func,

  identifier: PropTypes.string,
  reuseIdentifier: PropTypes.string,

  image: PropTypes.any,

  northeastCoordinate: PropTypes.shape({
    latitude: PropTypes.number.isRequired,
    longitude: PropTypes.number.isRequired,
  }).isRequired,

  southwestCoordinate: PropTypes.shape({
    latitude: PropTypes.number.isRequired,
    longitude: PropTypes.number.isRequired,
  }).isRequired,

  isNumberShown: PropTypes.bool,
  contentText: PropTypes.string,
  textSize: PropTypes.number,
  textColor: PropTypes.string,

  transparency: PropTypes.number,

  zIndex: PropTypes.number,
};

const defaultProps = {
  onPress() {},
};

class MapGroundOverlay extends React.Component {
  constructor(props) {
    super(props);
  }

  setNativeProps(props) {
    this.marker.setNativeProps(props);
  }

  _runCommand(name, args) {
    switch (Platform.OS) {
      case 'android':
        NativeModules.UIManager.dispatchViewManagerCommand(
          this._getHandle(),
          this.getUIManagerCommand(name),
          args
        );
        break;

      case 'ios':
        this.getMapManagerCommand(name)(this._getHandle(), ...args);
        break;

      default:
        break;
    }
  }

  render() {
    let image;
    if (this.props.image) {
      image = resolveAssetSource(this.props.image) || {};
      image = image.uri;
    }

      const AIRMapGroundOverlay = this.getAirComponent();

      return (
        <AIRMapGroundOverlay
           ref={ref => { this.marker = ref; }}
           {...this.props}
           identifier={this.props.identifier}
           onPress={this.props.onPress}
           image={image}
           isNumberShown={this.props.isNumberShown}
           contentText={this.props.contentText}
           textSize={this.props.textSize}
           textColor={this.props.textColor}
           transparency={this.props.transparency}
           zIndex={this.props.zIndex}
           style={[styles.marker, this.props.style]}
        />
    );
  }
}

MapGroundOverlay.propTypes = propTypes;
MapGroundOverlay.defaultProps = defaultProps;
MapGroundOverlay.viewConfig = viewConfig;

const styles = StyleSheet.create({
  marker: {
    position: 'absolute',
    backgroundColor: 'transparent',
  },
});

MapGroundOverlay.Animated = Animated.createAnimatedComponent(MapGroundOverlay);

module.exports = decorateMapComponent(MapGroundOverlay, {
  componentType: 'GroundOverlay',
  providers: {
    google: {
      ios: SUPPORTED,
      android: USES_DEFAULT_IMPLEMENTATION,
    },
  },
});
