// like_button.js
// Programmers: Nathan Vanos, Mason Dellutri, Joshua Go
'use strict';

const e = React.createElement;

class LikeButton extends React.Component {

  constructor(props) {
    super(props);
    this.state = { liked: false };
  }

  render() {
    if (this.state.liked) {    
      return e(
        'button',
        { onClick: () => this.setState({ liked: false }) },
        '\u274C'
      );
    }

    return e(
      'button',
      { onClick: () => this.setState({ liked: true }) },
      '\u2764'
    );
  }
}

const domContainer = document.querySelector('#like_button_container');
ReactDOM.render(e(LikeButton), domContainer);