import React, { Component } from 'react';
import {withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Table} from 'reactstrap';
import AppNavbar from './AppNavbar';
import SpotifyWebApi from 'spotify-web-api-js';


class GroupEdit extends Component {
    spotifyApi = new SpotifyWebApi();

    emptySong = {
        name: '',
        artist: '',
        link: '',
        userWhoAdded: 'Default'
    };

    getHashParams() {
        var hashParams = {};
        var e, r = /([^&;=]+)=?([^&;]*)/g,
            q = window.location.hash.substring(1);
        e = r.exec(q)
        while (e) {
            hashParams[e[1]] = decodeURIComponent(e[2]);
            e = r.exec(q);
        }
        console.log(hashParams);
        return hashParams;
    }

    constructor(props) {
        super(props);
        //const params = this.getHashParams();
        let params = this.getHashParams();
        let token = params.access_token;
        //console.log(token);
        if(token){
            this.spotifyApi.setAccessToken(token);
        }
        this.state = {
            options: [],
            song: this.emptySong,
            loggedIn: token ? true : false
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.query = this.query.bind(this);
        this.compare = this.compare.bind(this);
        this.getArtistName = this.getArtistName.bind(this);
    }


    handleChange(event) {
        const target = event.target;
        const id = target.id;
        let selectedOp = null;
        for (let i = 0; i < this.state.options.length; i++){
            if(this.state.options[i].id === id){
                selectedOp = this.state.options[i];
            }
        }
        console.log(selectedOp);
        this.setState({song : {
            artist: selectedOp.artists[0].name,
            name: selectedOp.name,
            link: selectedOp.external_urls.spotify,
            userWhoAdded: 'Robbie'

        }});
    }

    compare(a, b){
        if (a.popularity > b.popularity)
            return -1;
        if (a.popularity < b.popularity)
            return 1;
        return 0;
    }

    query(event){
        const target = event.target;
        const value = target.value;
        console.log(this.state);
        this.spotifyApi.searchTracks(value, {limit:25})
            .then(res => res.tracks.items)
            .then(json => {
                if (json === undefined) {
                    console.log("Invalid response.");
                    return [];
                } else {
                    for(let i = 0; i<json.length; i++){
                        json[i]['label'] = json[i].name + ' - ' + json[i].artists[0].name;
                        json[i]['value'] = i;
                    }
                    json.sort(this.compare);
                    this.setState({options: json});
                    return this.state.options;
                }
            }, err => {
                console.log(err.message);
                return [];
            });
    }

    async handleSubmit(event) {
        event.preventDefault();
        const item = this.state.song;
        console.log(this.state);
        await fetch('/api/add', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item),
        });
        this.props.history.push('/');
    }

    getArtistName(artists){
        let s = "";
        for(let i = 0; i < artists.length; i++){
            s += artists[i].name;
            s += " - "
        }
        s = s.substr(0, s.length-3);
        return s;
    }

    render() {
        const title = <h2>Song Search</h2>;

        const table = this.state.options.map(song => {
            return <tr key={song.value}>
                <td>
                    {song.name}
                </td>
                <td>{this.getArtistName(song.artists)}</td>
                <td>
                    <Button onClick={this.handleChange} color='primary' type='submit' id={song.id}>
                        ADD SONG
                    </Button>
                </td>
            </tr>
        });

        return <div>
            <AppNavbar/>
            <Container>
                {title}
                <Form onSubmit={this.handleSubmit} action='http://localhost'>
                    <FormGroup>
                        <Input
                            type='text'
                            name='name'
                            onChange = {this.query}
                            onKeyPress ={e =>{if (e.key === 'Enter') e.preventDefault();}}
                        />
                        <Table className='mt-4'>
                            <thead>
                            <tr>
                                <th>Name</th>
                                <th>Artist</th>
                                <th>Add Song</th>
                            </tr>
                            </thead>
                            <tbody>
                            {table}
                            </tbody>
                        </Table>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    }
}

//{/*<Async*/}
//    {/*valueKey="id"*/}
//    {/*labelKey="name"*/}
//    {/*placeholder="Add a Song..."*/}
// /   {/*loadingPlaceholder="Loading..."*/}
//    {/*cache={false}*/}
//    {/*loadOptions={this.query}*/}
//    {/*onChange={this.handleChange}*/}
//    {/*backSpaceRemoves*/}
//{/*/>*/}
// {console.log(this.state.options)}

export default withRouter(GroupEdit);