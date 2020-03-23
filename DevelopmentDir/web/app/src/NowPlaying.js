import React, { Component } from 'react';

class NowPlaying extends Component {
    constructor(props){
        super(props);
        this.state = {
                name: '',
                artist: '',
                link: '',
                user: '',
                likes: 0

        }
    }

    componentDidMount() {
        fetch('api/current', {
            header: {'Access-Control-Allow-Origin': '*'},
        })
            .then(response => response.json())
            .then((data) => this.setState({
                name: data['name'],
                artist: data["artist"],
                link: data["link"],
                user: data['userWhoAdded'],
                likes: data['likes']
            }));
    }

    render() {
        const divStyle = {
            background: '#5CDBA0',
            padding: 16
        }

        return (
            <div className="jumbotron" style={divStyle}>
                <p>Currently Playing:</p>
                <h2 className="text-info"><a href={this.state.link} target="_blank" rel="noopener noreferrer">{this.state.name}</a></h2>
                <h3 className="text-info">By: {this.state.artist}</h3>
            </div>
        )
    }
}

export default NowPlaying;