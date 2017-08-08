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

  identifier: PropTypes.string,
  reuseIdentifier: PropTypes.string,

  image: PropTypes.any,

  transparency: PropTypes.number,

  northeastCoordinate: PropTypes.shape({
    latitude: PropTypes.number.isRequired,
    longitude: PropTypes.number.isRequired,
  }).isRequired,

   southwestCoordinate: PropTypes.shape({
      latitude: PropTypes.number.isRequired,
      longitude: PropTypes.number.isRequired,
    }).isRequired,
};

const defaultProps = {
  onPress() {},
};

class MapGroundOverlay extends React.Component {
  constructor(props) {
    super(props);

    this.showCallout = this.showCallout.bind(this);
    this.hideCallout = this.hideCallout.bind(this);
  }

  setNativeProps(props) {
    this.marker.setNativeProps(props);
  }

  showCallout() {
      this._runCommand('showCallout', []);
    }

    hideCallout() {
      this._runCommand('hideCallout', []);
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
           image={image}
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
