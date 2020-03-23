import React, { Component } from 'react';
import { Button, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import NowPlaying from "./NowPlaying";

class SongList extends Component {

    constructor(props) {
        super(props);
        this.state = {queue: [], isLoading: true};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        this.setState({isLoading: true});

        fetch('api/list')
            .then(response => response.json())
            .then(data => this.setState({queue: data, isLoading: false}));
    }

    async remove(id) {
        await fetch(`/api/group/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedGroups = [...this.state.groups].filter(i => i.id !== id);
            this.setState({groups: updatedGroups});
        });
    }

    render() {
        const {queue, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        //<td>{song.userWhoAdded}</td>
        const songList = queue.map(song => {
            return <tr key={song.name}>
                <td>{song.name}</td>
                <td>{song.artist}</td>
            </tr>
        });

        const spotLogIn = 'http://' +window.location.hostname  + ':8888';
        console.log(spotLogIn);


        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <div className="float-right">
                        <Button className="alert-dark" href={spotLogIn}>Add Song</Button>
                    </div>
                    <h3>Smart Juke Box Pro</h3>
                    <br/>
                    <NowPlaying/>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="33%">Name</th>
                            <th width="33%">Artist</th>
                        </tr>
                        </thead>
                        <tbody>
                        {songList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );
    }
}

export default SongList;