class IdNode{
    parent
    children
    val
    constructor(val){
        this.parent=null;
        this.children=[];
        this.val=val;
    }
}

export class IdTree{
    root
    idLength
    //idChars = ['A','B','C'] // testArray
    idChars = ['A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z']
    /**
     * @param {number} idLength 
     * @param {string[]} idChars 
     */
    constructor(idLength, idChars){
        this.root=new IdNode(null);
        this.idLength = idLength;
        idChars? this.idChars = idChars : null
    }
    checkIdString(idString){
        if (typeof idString === 'string' && idString.length==this.idLength){
            var idArray = [...idString];
            for (var i=0; i<idArray.length; i++){
                if (!this.idChars.includes(idArray[i])) return new Error("id contains prohibited values");;
            }
        } else {
            return new Error("id doesn't match requerements");
        }
        return idArray;
    }
    pushId(idString){ //"ABC"
        var idArray = this.checkIdString(idString);
        if (idArray instanceof Error){ return idArray; }
        else { return this._pushId(idArray); }
    }
    _pushId(idArray){ // ['A','B','C']
        if (Array.isArray(idArray)){
            var visitor = this.root;
            var value, doCreateNode;
            while(value=idArray.pop()){ // for each char in id
                doCreateNode = true;
                for (var i=0; i<visitor.children.length; i++){ // for each child
                    var child = visitor.children[i];
                    if (child instanceof IdNode){
                        if (child.val==value){
                            doCreateNode = false;
                            if (idArray.length==0) return new Error("failed: ID already exists")
                            visitor = child;
                            i=0;            /*?*/
                            break; // next child iteration
                        }
                    } else return new Error("error1")
                }
                if (doCreateNode){
                    var newIdNode = new IdNode(value);
                    newIdNode.parent = visitor;
                    visitor.children.push(newIdNode);
                    visitor = newIdNode;
                }
            }
        } else return "error0"
        return "success"
    }
    getFreeId(){
        var idChars = this.idChars;
        var idLength = this.idLength;
        var lvl = 0;
        var freeId=[];
        var isFound=false;
        function _checkNode(node){ // recursive
            if (node instanceof IdNode){
                if (!isFound){
                    //
                    if(lvl<idLength){
                        var used=[];
                        var val;
                        for (var i=0; i<node.children.length; i++){
                            lvl++;
                            val = _checkNode(node.children[i]);
                            lvl--;
                            if (isFound) {
                                if (val) freeId.push(val);
                                return node.val;
                            } else used.push(val);
                        }
                        
                        var unused = idChars.filter(element => !used.includes(element));
                        if (unused.length>0){ //!!
                            
                            var newIdNode = new IdNode(unused[0]);
                            newIdNode.parent=node;
                            node.children.push(newIdNode);
                            
                            if (lvl==idLength-1){
                                isFound=true;
                                freeId.push(unused[0]);
                            } else {
                                _checkNode(node) // check same again (but now with fresh child)
                            }
                        }
                    }
                    //
                }
                return (node.val); // for the branch top to visit (according to idLength) 
            }
        }
        _checkNode(this.root);
        return freeId.join('');
    }
    /**
     * @param {string} id 
     * @returns 
     */
    deleteId(id){
        var idArray = this.checkIdString(id);
        if (idArray instanceof Error) {return idArray}
        else {
            var visitor = this.root;
            var isFound = false;
            while (idArray.length!=0){
                var val = idArray.pop();
                for (var i=0; i<visitor.children.length;i++){
                    var child = visitor.children[i];
                    if (child instanceof IdNode && child.val==val){
                        if (idArray.length==0){
                            isFound=true;
                            visitor.children.splice(i,1);
                            child=null;
                        }
                        visitor = child;
                        break;
                    }
                }
            }
            if (isFound){return "deleted successfully"}
            else {return new Error("failed: ID wasn't found")}
        }
    }
    log() {
        this._visualizeNode(this.root, 0);
    }
    _visualizeNode(node, depth) {
        const indentation = '  '.repeat(depth);
        console.log(`${indentation}${node.val}`);
        
        for (const child of node.children) {
            if (child instanceof IdNode) {
                this._visualizeNode(child, depth + 1);
            } else {
                console.error('Error: Child is not an instance of IdNode');
            }
        }
    }
}