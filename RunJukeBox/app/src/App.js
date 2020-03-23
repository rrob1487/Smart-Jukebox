import React, { Component } from 'react';
import './App.css';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import SongList from './SongList';
import GroupEdit from './GroupEdit';

class App extends Component {
    render() {
        return (
            <div>
            <Router className="col-12">
                <Switch>
                    <Route path='/' exact={true} component={SongList}/>
                    <Route path='/list' exact={true} component={SongList}/>
                    <Route path='/list/:id' component={GroupEdit}/>
                </Switch>
            </Router>
            </div>
        )
    }
}

export default App;